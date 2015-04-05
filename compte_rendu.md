# Introduction #

Ce projet est réalisé dans le cadre de nos études, pour la matière d'IHM.

# Problèmes rencontrés #

  * API Google Direction trop limité en nombre de requêtes; nécessité de relancer une requête après 200 ms, du fait du refus de celle-ci (à cause d'un trop grand nombre de requêtes).

  * Le trop grand nombre de requêtes effectuées peut provoquer un ralentissement du jeu. Toutefois, le rafraîchissement des positions sur le carte est effectué en fonction du temps depuis le dernier rafraîchissement et de la vitesse de chaque entité; il y a donc abstraction de la puissance du téléphone de l'utilisateur.

  * Problème d'accès concurrents du fait de l'utilisation de plusieurs threads. **Problème résolu**.

# Améliorations futures #

  * Refactor du code.

  * Meilleure gestion des requêtes : possibilité de réutilisation d'anciennes routes pour minimiser le nombre de requêtes (à voir toutefois à ne pas surcharger la mémoire du téléphone).

  * Ajout de nouveaux items de jeu.

  * Meilleurs succès : affichage de la progression des succès au moyen de progress bar.

  * Amélioration des différents écrans : le titre des sections est dans l'image de fond, alors qu'il devrait être dans un composant graphique afin d'éviter que les boutons ne passe par dessus, lorsque l'écran est trop petit. (Compatibilité avec les petits et grands écrans -- tablettes)