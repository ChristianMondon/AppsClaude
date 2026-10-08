# Plan – Chat & Souris

| | |
|---|---|
| Statut | **Validé par Christian le 2026-10-08** |
| Basé sur | `02-specification-technique.md` |
| Méthode | Une tâche = un petit commit ; une PR par jalon ; essai sur la tablette à la fin de chaque jalon |

Règle : après la validation de ce plan, chaque jalon suit **tests d'abord pour la logique**, puis rendu, puis CI verte, puis APK à essayer par Christian avant de passer au jalon suivant.

## Jalon M1 – Cœur de jeu
Objectif : un chat qui court et saute, une souris à rattraper, victoire et défaite, avec barrières, trous et lave.
- [x] T1.1 Squelette Gradle / Android, manifeste paysage plein écran, workflow CI `chatsouris.yml`
- [x] T1.2 `Params`, `Model`, `step` : avancée du chat, gravité, saut, tampon de saut (+ tests US1, US2)
- [x] T1.3 Souris, écart, victoire / défaite, blocage en fin de piste (+ tests US7, US8, US9)
- [x] T1.4 Obstacles sautables, trébuchement, invulnérabilité (+ tests US3)
- [x] T1.5 `LevelDef` niveau 1 et `LevelGenerator` avec invariants 1, 2, 4, 5 (+ tests)
- [x] T1.6 Palette, sprites du chat, de la souris et des obstacles, conversion en bitmaps, rendu avec parallaxe et agrandissement entier
- [x] T1.7 Boucle de jeu à pas fixe, toucher pour sauter, indicateur d'écart, écran de fin minimal
- [x] T1.8 Robot de simulation : le niveau 1 est gagné par un joueur parfait (test)

**Essai tablette M1** : le saut est agréable, le niveau 1 se gagne et se perd, 60 images/s.

## Jalon M2 – Outils, locomotions, bouclier
- [ ] T2.1 Arbres et murs, outils tronçonneuse et marteau (+ tests US4, US5)
- [ ] T2.2 Objets et ramassage ; locomotions skateboard, rollers, vélo (+ tests US6)
- [ ] T2.3 Écureuils, noix, bouclier (+ tests US13)
- [ ] T2.4 Générateur : objets, écureuils, invariants 3 et 6 (+ tests de solvabilité)
- [ ] T2.5 Sprites des arbres, murs, objets, écureuil, noix, bouclier ; affichage des outils et de la locomotion actifs
- [ ] T2.6 Robot de simulation étendu (utilise outils et saute les noix)

**Essai tablette M2** : chaque objet est reconnaissable et utile ; les écureuils sont lisibles.

## Jalon M3 – Niveaux et écrans
- [ ] T3.1 Niveaux 2 et 3, test de cohérence des définitions et de faisabilité (robot) (+ tests US10)
- [ ] T3.2 Écran d'accueil avec sélection de niveaux et verrouillage
- [ ] T3.3 Écran de fin complet (temps, rejouer, niveau suivant, accueil)
- [ ] T3.4 Pause (bouton et mise en pause automatique en arrière-plan) (US12)

**Essai tablette M3** : on peut jouer les trois niveaux d'affilée, sans plantage sur 10 parties.

## Jalon M4 – Finition
- [ ] T4.1 Sauvegarde de la progression et des meilleurs temps (+ tests US11)
- [ ] T4.2 Animations supplémentaires (trébuchement, victoire, alerte de l'écureuil)
- [ ] T4.3 Sons et musique (spécification courte au démarrage de la tâche)
- [ ] T4.4 Icône de l'application et réglage fin de l'équilibrage (`Params`)
- [ ] T4.5 Revue finale (code-review), mise à jour de la documentation

**Essai tablette M4** : validation finale par Christian et sa famille.

## Définition de « terminé » pour une tâche
Tests unitaires verts en local (JVM) et sur la CI, code relu, documentation à jour, commit unique.

## Points d'attention
- Le rendu Compose ne se teste pas sans appareil : la CI le compile, Christian le valide sur la tablette.
- L'équilibrage (vitesses, durées) sera ajusté après les essais, sans toucher aux règles.
- Le SDK Android n'est pas disponible dans mon environnement : je vérifie la logique en JVM, la CI compile l'application.
