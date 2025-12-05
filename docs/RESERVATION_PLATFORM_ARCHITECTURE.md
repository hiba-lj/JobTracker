# Plateforme de réservation basée sur Spring Cloud

## Objectifs
- Offrir une architecture microservices conforme aux standards Spring Cloud pour une plateforme de réservation (hôtels, salles, terrains).
- Séparer clairement la gestion des ressources, des réservations et l'exposition d'une API unifiée.
- Centraliser la configuration, la découverte de services, le routage, la résilience et l'observabilité.

## Découpage des microservices
### Resource Service (Gestion des ressources)
- **Responsabilité** : CRUD des ressources (hôtel, salle, terrain, chambres, créneaux), gestion des capacités, statuts de disponibilité.
- **Stockage** : Base relationnelle dédiée (MySQL/PostgreSQL). Schéma conseillé : `resource` (type, localisation, attributs), `unit` (chambre/salle/terrain), `amenity`.
- **API** :
  - `POST /resources` créer une ressource et ses unités.
  - `GET /resources/{id}` consulter une ressource.
  - `GET /resources/{id}/availability` disponibilité sur période.
  - `PATCH /resources/{id}` mise à jour partielle.
- **Intégration** : Expose des DTO stables pour l'appel Feign depuis Reservation Service.

### Reservation Service (Gestion des réservations)
- **Responsabilité** : Orchestration des réservations, règles métier (collisions d'horaires, annulation, pénalités), statut de paiement simulé.
- **Stockage** : Base relationnelle distincte (MySQL/PostgreSQL). Tables clés : `booking`, `booking_item`, `payment_attempt`.
- **API** :
  - `POST /bookings` création (vérification disponibilité via Feign vers Resource Service).
  - `GET /bookings/{id}` détail d'une réservation.
  - `PATCH /bookings/{id}/status` changement de statut (confirmée, annulée, expirée).
  - `POST /bookings/{id}/payments/simulate` mise à jour de l'état de paiement (simulé).
- **Résilience** : Circuit breaker/Retry TimeLimiter (Resilience4j) autour des appels Feign vers Resource Service.

### Interface Service (Front API)
- **Responsabilité** : Façade pour clients web/mobile, agrégation des données venant des deux services métier, et exposition d'un parcours simplifié.
- **Fonctions clés** :
  - Catalogue agrégé : `GET /catalog` combine ressources + disponibilité.
  - Parcours de réservation : `POST /orders` crée une réservation via Reservation Service après pré-vérification.
  - Simulation de paiement : `POST /orders/{id}/pay` forward vers Reservation Service.
- **Sécurité** : Spring Security + JWT (Gateway peut valider les tokens). Possibilité d'avoir un Provider interne simple (in-memory/Keycloak futur).

## Services d'infrastructure Spring Cloud
- **Config Server** :
  - Branch `config` d'un dépôt Git dédié.
  - Fichiers `application.yml`, `resource-service.yml`, `reservation-service.yml`, `interface-service.yml`, `gateway.yml`, `eureka.yml`.
  - Active profiles pour environnements (`dev`, `prod`).
- **Eureka Server** :
  - Enregistrement automatique des trois microservices, Config Server, Gateway.
  - `prefer-ip-address=true` pour conteneurs.
- **API Gateway (Spring Cloud Gateway)** :
  - Routage dynamique via Eureka (`lb://resource-service`, etc.).
  - Filtres globaux : logging des requêtes, propagation du `Correlation-Id`, validation JWT, rate limiting Redis (optionnel).
  - Mappages recommandés :
    - `/api/resources/** -> lb://resource-service`
    - `/api/bookings/** -> lb://reservation-service`
    - `/api/interface/** -> lb://interface-service`
- **Actuator & Observabilité** :
  - Exposer `/actuator/health`, `/actuator/metrics`, `/actuator/prometheus` (Micrometer + Prometheus). Dashboards Grafana optionnel.
  - Tracing distribué : OpenTelemetry exporter OTLP (ou Zipkin).
- **Résilience** : Resilience4j dans les clients Feign + Gateway retries (conservateur).

## Communication et contrats
- **OpenFeign** :
  - Interface dans Reservation Service pour appeler Resource Service : `ResourceAvailabilityClient.getAvailability(resourceId, from, to)`.
  - Propagation des en-têtes de corrélation et de sécurité (Feign interceptor).
- **DTO/Mapping** :
  - DTO partagés via module Maven/Gradle commun ou publication dans un registre d'artifacts (éviter le partage de code source direct entre services).
  - Validation via Bean Validation (Jakarta). 
- **Événements (optionnel)** :
  - Publication d'événements (Kafka/RabbitMQ) pour notifications ou synchronisation de caches.

## Données et migrations
- Chaque service possède sa base.
- Migrations avec Flyway ou Liquibase dans chaque service (`src/main/resources/db/migration`).
- Jeux de données de démarrage (profils `dev`) pour ressources et réservations.

## Sécurité
- JWT signé (HS256 pour démarrage, clé en Config Server chiffrée avec Spring Cloud Config + Vault optionnel).
- Gateway valide le token avant routage; les services internes revalident les rôles.
- Rôles suggérés : `ROLE_USER` (réserver), `ROLE_ADMIN` (gérer ressources et annulations).

## Déploiement et DevOps
- **Packaging** : Dockerfile par service, images légères (distroless/Alpine). 
- **Composition** : docker-compose pour dev incluant Config Server, Eureka, Gateway, services, bases de données, Prometheus/Grafana éventuels.
- **CI/CD** : pipeline avec étapes lint/test, build images, scan, déploiement sur registre.
- **Configuration sensible** : variables d'environnement ou Vault; ne pas versionner de secrets.

## Flux utilisateur principal
1. Le client appelle la Gateway `/api/interface/catalog`.
2. Interface Service agrège les ressources depuis Resource Service (Feign) et renvoie la disponibilité.
3. Le client crée une réservation via `/api/interface/orders` ; Interface Service orchestre un appel vers Reservation Service.
4. Reservation Service vérifie la disponibilité auprès de Resource Service (Feign), crée l'entrée `booking`, retourne un statut `PENDING`.
5. Le client déclenche `/api/interface/orders/{id}/pay` ; Reservation Service simule le paiement et confirme la réservation.

## Checklist de démarrage
- [ ] Config Server opérationnel avec repo Git dédié.
- [ ] Eureka Server lancé et accessible par tous les services.
- [ ] Gateway configurée avec routage dynamique et sécurité.
- [ ] Resource Service, Reservation Service, Interface Service enregistrés dans Eureka.
- [ ] Actuator exposé et observabilité branchée (Prometheus/OTel).
- [ ] Tests de bout en bout via la Gateway (création de ressource, réservation, paiement simulé).
