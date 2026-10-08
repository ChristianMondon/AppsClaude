# Spécification – Morpion

## Objectif
Jeu de morpion (tic-tac-toe) 3×3 à deux joueurs sur un même téléphone.

## Règles
- X commence, puis alternance avec O.
- Toucher une case vide la marque ; une case occupée est ignorée.
- Victoire : 3 symboles identiques alignés (ligne, colonne, diagonale).
- Égalité : 9 cases remplies sans vainqueur.
- Après la fin de partie, plus aucun coup n'est accepté.

## Critères d'acceptation
1. Un coup valide place le symbole du joueur courant et passe la main.
2. Un coup sur une case occupée ne change pas l'état.
3. Les 8 alignements gagnants sont détectés.
4. Une grille pleine sans alignement donne une égalité.
5. Aucun coup après victoire/égalité.
6. L'écran affiche le tour courant ou le résultat, met en évidence la ligne gagnante, et un bouton « Rejouer » remet à zéro.
7. Un compteur de victoires X / O / égalités persiste pendant la session.

## Hors périmètre
IA adverse, multijoueur en ligne, sons, persistance disque.
