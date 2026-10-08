# Expression de besoin – Chat & Souris

| | |
|---|---|
| Statut | **Brouillon à valider** |
| Date | 2026-10-08 |
| Auteur | Claude, d'après les échanges avec Christian Mondon |
| Étape suivante | PRD (`01-prd.md`), après validation de ce document |

> Les points marqués **[Hypothèse]** n'ont pas été confirmés : ils sont à valider ou corriger. Les questions ouvertes sont listées en fin de document.

## 1. Contexte et problème
Christian souhaite un jeu mobile Android simple, à prise en main immédiate, avec un univers amusant. Il s'agit d'un projet personnel, mené avec une méthode « AI-native SDLC » : besoin → PRD → spécification → plan → code → tests → revue.

## 2. Idée en une phrase
Un chat court derrière une souris et doit **sauter par-dessus des obstacles** (barrières, trous, flaques de lave), **ramasser des objets** pour franchir ceux qui ne se sautent pas (tronçonneuse pour les arbres, marteau pour les murs) et **des moyens de locomotion** (skateboard, vélo, rollers) pour aller plus vite, jusqu'à **attraper la souris**.

## 3. Utilisateurs
- **Utilisateur principal [Hypothèse]** : Christian, puis son entourage (famille, amis), tout public, enfants compris.
- **Appareils** : tablette et téléphone Android.
- **Compétence** : aucune ; le jeu doit se comprendre sans notice.

## 4. Besoins fonctionnels
| # | Besoin | Priorité |
|---|---|---|
| BF1 | Le joueur contrôle un chat qui avance automatiquement dans un décor défilant. | Indispensable |
| BF2 | Le joueur fait sauter le chat d'une simple action (toucher l'écran). | Indispensable |
| BF3 | Une souris fuit devant le chat ; l'objectif est de la rattraper. | Indispensable |
| BF4 | Des obstacles à sauter : barrières, trous, flaques de lave. | Indispensable |
| BF5 | Des obstacles qui ne se sautent pas : arbres (à couper) et murs (à casser). | Indispensable |
| BF6 | Des objets à ramasser : tronçonneuse (arbres), marteau (murs). | Indispensable |
| BF7 | Des moyens de locomotion à ramasser : skateboard, vélo, rollers (vitesse accrue). | Indispensable |
| BF8 | Une issue claire à la partie : victoire (souris attrapée) ou défaite (souris perdue). | Indispensable |
| BF9 | Un retour visuel de la distance entre le chat et la souris. | Important |
| BF10 | Plusieurs niveaux de difficulté croissante. | Important |
| BF11 | Sauvegarde de la progression et des meilleurs scores. | Souhaitable |
| BF12 | Sons et musique. | Souhaitable |
| BF13 | Rendu en pixel art : sprites nets, palette réduite, sans lissage. | Indispensable |

## 5. Besoins non fonctionnels
- **Prise en main** : jouable dès la première seconde, une seule commande.
- **Fluidité** : 60 images/s sur une tablette Android récente [Hypothèse : Android 7.0 / API 24 minimum].
- **Hors ligne** : aucun compte ni connexion requis [Hypothèse].
- **Public** : contenu adapté aux enfants (pas de violence graphique, pas de publicité intrusive).
- **Qualité** : logique de jeu testée automatiquement ; build et APK produits par la CI GitHub.
- **Style graphique** : pixel art (décisions de Christian) ; personnages et décors originaux, sans personnage de marque.

## 6. Contraintes
- Plateforme : Android natif, Kotlin + Jetpack Compose [Hypothèse, cohérent avec les projets précédents du dépôt].
- Développement dans le dépôt `AppsClaude`, dossier `chatsouris/`.
- Test sur tablette via l'APK produit par la CI (pas d'émulateur local).
- Pas de budget ni de date imposés [Hypothèse].
- Usage personnel ; une publication sur le Play Store demanderait un compte développeur et des étapes supplémentaires (hors périmètre).

## 7. Critères de réussite
1. Une partie complète (démarrage → victoire ou défaite) se joue sans explication.
2. Les trois familles d'éléments (obstacles à sauter, objets-outils, locomotions) sont présentes et reconnaissables.
3. Chaque objet a un effet visible et utile.
4. Le jeu tourne de façon fluide sur la tablette de Christian.
5. L'APK s'installe depuis GitHub Actions et le jeu se lance sans plantage.

## 8. Hors périmètre
Multijoueur, achats intégrés, publicité, classement en ligne, publication sur le Play Store, version iOS.

## 9. Questions ouvertes
1. Public et appareils : bien « vous et votre entourage » sur tablette et téléphone ?
2. Vue du jeu : 2D de côté (comme un jeu de plateforme défilant) vous convient-elle ? [Hypothèse retenue dans le PRD]
3. Un niveau se termine-t-il quand on attrape la souris (niveaux de longueur finie), ou veut-on aussi un mode sans fin ?
4. Faut-il prévoir d'autres objets plus tard (ex. aimant, bouclier) ?
5. ~~Ambiance graphique~~ : **réponse de Christian : pixel art.** *(Résolue)*
