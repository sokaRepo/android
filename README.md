#LoginMeToMyAPI

###Mobile App
Application mobile pour téléphones Android. L'application permet de se connecter à l'API développée (/api).

####Propriétés
- Une activité permettant de se connecter avec interface de login, avec le mode landscape et portrait
- Un IntentService qui se connecte à api.mobile.crashlab.org pour se logger et récupérer une liste d'utilisateurs dans un fichier JSON
- Notification via Toast pour indiquer à l'utilisateur le status de son login (valide, invalide, pas de connexion internet)
- Une deuxième activité qui est accessible lorsque l'utilisateur se connecte avec un login/mdp valides
- Sauvegarde du nom d'utilisateur de l'utilisateur dans une base de données SQLite locale

###Python API
API Python permettant d'ajouter/récupérer des utilisateurs dans une base de données SQLite.
Pour plus d'informations voir api/README.md