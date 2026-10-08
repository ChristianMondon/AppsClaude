# Spécification – BlocMon

## Objectif
Jeu Android solo : explorer un monde en blocs (style voxel, vue de dessus 2D) et capturer des créatures dans les hautes herbes.

## Monde
- Grille 48×48 générée de façon déterministe à partir d'une graine.
- Blocs : herbe, haute herbe, sable (marchables) ; eau, pierre, arbre (obstacles).
- Le joueur démarre au centre, sur une zone toujours marchable.

## Déplacement
- Flèches directionnelles : un pas par appui dans 4 directions.
- Un obstacle ou le bord de la carte bloque le pas (rien ne change).
- La vue est un carré de 9×9 blocs centré sur le joueur.

## Rencontres et capture
- Chaque pas sur de la haute herbe déclenche une rencontre avec 25 % de chance.
- L'espèce est tirée selon une rareté : Herbon 50, Aquaby 30, Pierrot 15, Flamblo 5.
- Pendant une rencontre, le joueur ne peut pas se déplacer ; il choisit « Lancer la balle » ou « Fuir ».
- Lancer consomme une balle. Réussite selon le taux de capture de l'espèce ; sinon la créature s'enfuit avec 30 % de chance, ou reste.
- Départ avec 10 balles ; +1 balle tous les 20 pas (maximum 10).
- Sans balle, impossible de lancer (on peut seulement fuir).

## Critères d'acceptation
1. Même graine → même monde ; graine différente → monde différent.
2. Le point de départ est marchable.
3. Un obstacle ou le bord bloque le pas ; un pas valide met à jour la position et le compteur de pas.
4. Rencontre uniquement sur haute herbe, selon la probabilité de 25 %.
5. Aucun déplacement pendant une rencontre.
6. Lancer : consomme une balle, capture ou non selon le taux, fuite possible ; impossible sans balle.
7. Fuir termine la rencontre sans capture.
8. Régénération d'une balle tous les 20 pas, plafonnée à 10.
9. L'écran affiche le monde, les balles, les créatures capturées, et une fenêtre de rencontre.

## Hors périmètre (v1)
Combat, créatures visibles qui se déplacent, sauvegarde sur disque, 3D, sons.
