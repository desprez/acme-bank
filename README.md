# Présentation

Cette application est un exemple d'application MicroService orientée DDD permettant de simuler les actions sur un compte bancaire.
Elle constitue un MVP volontairement simpliste.

Voici les besoins tel que le "métier" aurait pu les exprimer :

### Cas d'utilisation :
  - un utilisateur peut créer un nouveau compte.
  - un utilisateur peut fermer un compte Actif ou Suspendu.
  - un utilisateur suspend un compte Actif.
  - un utilisateur effectue un dépot d'argent sur un compte.
  - un utilisateur consulte le solde d'un compte bancaire.
  - un utilisateur effectue un retrait d'argent sur un compte.
  - un utilisateur effectue un transfert d'argent d'un compte sur un autre compte.
  - un batch permet d'exporter la liste des comptes comprenant l'iban, le solde, le statut et la devise.

### Règles de gestion

- Un compte bancaire peut être retrouvé uniquement par son IBAN.
- Lors de la création d'un compte un IBAN lui est affecté automatiquement.
- Tous les montants doivent être exprimés dans une devise.
- la fermeture d'un compte ne peut se faire que si le solde est à 0 et que son statut est soit Ouvert soit Suspendu.
- Les retraits ne sont possibles que si le solde le permet.
- Les retraits ne peuvent se faire que sur un compte Actif.
- Les dépots ne peuvent se faire que sur un compte Actif ou Supendu.
- En attendant l'implémentation d'un module de conversion de devise, les opérations doivent donc se faire dans la devise du compte sous peine d'être rejetées.

# Structuration

Cette application est basée sur SpringBoot.

Elle est composée d'un projet parent et de 5 sous-modules :

- bank-account-domain
- bank-account-application
- bank-account-exposition
- bank-account-infrastructure
et
- bank-account-batch (ce module constitue l'exposition du service dans un mode "Batch" (non implémenté pour le moment)

La couche exposition utilise [OpenAPI-Spec](https://github.com/swagger-api/swagger-core) et Springfox (https://github.com/springfox/springfox) afin de documenter les services exposés.
(Elle est optionnelle, supprimer ce projet n'expose pas une API)

La couche batch utilise Spring Batch
(Elle est optionnelle, supprimer ce projet si l'application ne comprend pas de batch)

# Packaging

L'artifact produit par le build est un fichier "WAR" déployable sur un serveur Websphere liberty core.

# Démarrer l'application Web

1) Depuis maven dans le projet "bank-account-exposition" :

	mvn clean spring-boot:run

2) Depuis Eclipse dans le projet "bank-account-exposition" :

classe : com.acmebank.demo.bank.account.app.exposition.BankAccountApplication

Run as java application

	url : http://localhost:8080/T1

3) Sur Liberty core, déposer le war dans le répertoire dropins et relancer le serveur


# Documentation API
L'interface swagger-ui est visible via l'url : http://localhost:8080/T1/swagger-ui.html

le swagger au format JSON est disponible via cet url : http://localhost:8080/T1/v2/api-docs


# Paramétrage
Les paramètres de l'application (port, contextPath, etc...) peuvent être modifiés dans le fichier application.yml

Ce fichier peut être externalisé sur le serveur Liberty dans un répertoire /config

# Notes d'implémentation

* Le projet "bank-account-domain" contient des tests unitaires et des scénarios cucumber tels que le BA aurait pu les exprimer.
* Le projet "bank-account-application" contient des tests unitaires bouchonnés.
* Le projet "bank-account-exposition" contient des tests d'API correspondant à des scénarios cucumber tels que le BA les a exprimé.
* Le projet "bank-account-infrastructure" contient des tests unitaires.

* La base de donnnées utilisée est une base de donnée en mémoire (H2).

* Spring-jpa est utilisé dans la couche infrastructure et le mapping est implémenté via le fichier META-INF/orm.xml afin de ne pas inclure de dépendance JPA dans la couche domain.

* Les outils de génération de code "boiler plate" tels que lombok et mapstruct n'ont pas été utilisé volontairement :
	- afin de ne pas nuire à lisibilité et la navigation dans le code (pour cette application à but éducatif)
	- car ils sont très IDE dépendants (ici pas besoin de confuguration supplémentaire ni de plugin)

## Diagramme de classes de la couche domain

![alt text](bank-account-domain/src/main/resources/Class%20Diagram.png "Class Diagram")

# Next steps

Dans les prochains sprints (à prioriser avec le PO)
- Implémentation du convertisseur de devise
- Produire un message (pour les virements sur les comptes externe par exemple).
- Consommer un message (ex : pour les opération courantes)
- Implémentation du journal des opérations et sa consultation via API
