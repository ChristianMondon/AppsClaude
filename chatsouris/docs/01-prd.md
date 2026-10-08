# PRD – Chat & Souris

| | |
|---|---|
| Statut | **Brouillon à valider** |
| Date | 2026-10-08 |
| Basé sur | `00-expression-de-besoin.md` |
| Étape suivante | Spécification technique et plan, après validation |

> Les choix marqués **[Proposition]** sont des décisions de conception proposées par Claude, à confirmer ou modifier.

## 1. Vision
Un jeu de course-poursuite drôle et immédiat : le chat bondit, tronçonne, casse et roule pour rattraper la souris.

## 2. Objectifs
| Objectif | Mesure |
|---|---|
| Un jeu amusant dès la première partie | Une personne qui n'a jamais joué finit le niveau 1 sans explication |
| Gameplay lisible | Chaque obstacle a une réponse évidente (sauter, couper, casser) |
| Qualité technique | Tests unitaires verts ; APK produit par la CI à chaque PR |
| Fluidité | ≥ 60 images/s sur la tablette cible |
| Identité visuelle | Pixel art cohérent (palette et taille de grille uniques) |

**Non-objectifs (v1)** : multijoueur, publicité, achats, classement en ligne, Play Store.

## 3. Personas
- **Christian, joueur et créateur** : veut essayer le jeu sur sa tablette et le faire évoluer.
- **Un membre de la famille, enfant compris** : joue 5 minutes, sans lire de règles.

## 4. Principe de jeu
- Défilement horizontal automatique, vue 2D de côté *(validé)*.
- Le chat avance seul ; **un seul geste : toucher l'écran pour sauter**.
- La souris court devant. Un **indicateur d'écart** montre la distance chat-souris.
- Le chat rattrape la souris quand l'écart tombe à zéro : **victoire du niveau**.

### 4.1 Obstacles
| Obstacle | Comment le passer | Si raté |
|---|---|---|
| Barrière | Sauter | Trébuchement |
| Trou | Sauter | Chute : trébuchement et retour sur le bord |
| Flaque de lave | Sauter | Brûlure : trébuchement |
| Arbre | **Tronçonneuse** (trop haut pour sauter) | Trébuchement |
| Mur | **Marteau** (trop haut pour sauter) | Trébuchement |

### 4.2 Objets
| Objet | Effet | Durée **[Proposition]** |
|---|---|---|
| Tronçonneuse | Coupe automatiquement le prochain arbre rencontré | 1 usage |
| Marteau | Casse automatiquement le prochain mur rencontré | 1 usage |
| Skateboard | Vitesse ×1,3 | 8 s |
| Rollers | Vitesse ×1,5 | 6 s |
| Vélo | Vitesse ×1,8 | 5 s |

- On porte au plus un outil de chaque type ; l'outil ramassé s'affiche à l'écran.
- Une locomotion remplace la précédente si on en ramasse une nouvelle.

### 4.3 Trébuchement, victoire, défaite
- Un trébuchement **ralentit le chat un court instant** : l'écart avec la souris augmente.
- **Défaite** : l'écart dépasse une limite maximale (la souris s'échappe) **[Proposition]**. Pas de système de vies.
- **Victoire** : l'écart tombe à zéro.
- La souris accélère légèrement au fil du niveau.

## 5. Exigences fonctionnelles et user stories
| ID | En tant que joueur, je veux… | Critères d'acceptation |
|---|---|---|
| US1 | faire sauter le chat en touchant l'écran | Un toucher déclenche un saut ; on ne peut pas sauter en l'air ; la hauteur est constante |
| US2 | voir le chat avancer seul dans un décor qui défile | Défilement continu ; la vitesse dépend de la locomotion active |
| US3 | franchir barrières, trous et flaques de lave en sautant | Sauter au bon moment passe l'obstacle ; sinon trébuchement |
| US4 | ramasser une tronçonneuse pour couper un arbre | Sans tronçonneuse, l'arbre fait trébucher ; avec, il est coupé et l'outil consommé |
| US5 | ramasser un marteau pour casser un mur | Même règle que US4 avec le marteau et le mur |
| US6 | ramasser un skateboard, des rollers ou un vélo pour aller plus vite | La vitesse augmente selon l'objet, pour la durée prévue, puis revient à la normale |
| US7 | voir l'écart entre le chat et la souris | Un indicateur se met à jour en continu |
| US8 | gagner en attrapant la souris | Écart à zéro → écran de victoire avec « Niveau suivant » et « Rejouer » |
| US9 | perdre si la souris s'échappe | Écart maximal dépassé → écran de défaite avec « Rejouer » |
| US10 | choisir un niveau parmi plusieurs de difficulté croissante | Au moins 3 niveaux ; chacun plus dense et plus rapide |
| US11 | retrouver ma progression | Niveaux débloqués et meilleur temps conservés après fermeture de l'app (Souhaitable) |
| US12 | mettre en pause | Un bouton met le jeu en pause et le reprend |

## 5 bis. Direction artistique : pixel art
Décision de Christian : le jeu est en **pixel art**.
- Sprites en grille de **16×16 pixels** (chat, souris, objets, obstacles) agrandis par un facteur entier, **sans lissage** pour garder des pixels nets **[Proposition]**.
- **Palette réduite** (une quinzaine de couleurs) commune à tout le jeu **[Proposition]**.
- Décor en tuiles répétables (sol, ciel, fond lointain avec défilement plus lent).
- Animations en quelques images : course du chat, saut, trébuchement.
- **Production des images [Proposition]** : les sprites sont dessinés par Claude sous forme de grilles de pixels dans le code (pas de fichier image externe), faciles à retoucher ; Christian peut les remplacer plus tard par ses propres images.

## 6. Écrans
1. **Accueil** : titre, « Jouer », choix du niveau.
2. **Jeu** : décor défilant, chat, souris, indicateur d'écart, outil et locomotion actifs, bouton pause.
3. **Fin de niveau** : victoire ou défaite, temps, actions (rejouer, suivant, accueil).

## 7. Niveaux
| Niveau | Longueur | Densité d'obstacles | Objets |
|---|---|---|---|
| 1 | Courte | Faible, barrières et trous seulement | Skateboard |
| 2 | Moyenne | Moyenne, ajout de lave et d'arbres | + tronçonneuse, rollers |
| 3 | Longue | Forte, tous les obstacles | Tous les objets |

Les niveaux sont définis par une graine et des paramètres, de sorte que le niveau soit reproductible et testable **[Proposition]**.

## 8. Exigences non fonctionnelles
Reprises du besoin : une seule commande, 60 images/s, hors ligne, tout public, contenu original, logique testée, APK via CI.

## 9. Indicateurs de succès
- Niveau 1 réussi par un nouveau joueur en moins de 3 essais.
- Aucun plantage pendant 10 parties consécutives sur la tablette.
- Tous les critères d'acceptation US1 à US10 couverts par des tests automatiques sur la logique.

## 10. Risques
| Risque | Impact | Parade |
|---|---|---|
| Saut peu précis sur tactile | Frustration | Marge de tolérance sur les collisions, tests sur la tablette |
| Fluidité avec Compose Canvas | Saccades | Boucle de jeu à pas fixe, peu d'objets à l'écran, mesure tôt |
| Difficulté mal calibrée | Jeu trop dur ou trop facile | Paramètres des niveaux faciles à ajuster |
| Qualité du pixel art | Rendu amateur ou incohérent | Palette et taille de grille uniques, sprites relus sur la tablette, remplaçables ensuite par de vrais fichiers image |

## 11. Jalons proposés
1. **M1 – Cœur de jeu** : chat, saut, défilement, souris, écart, victoire/défaite (obstacles sautables).
2. **M2 – Outils et locomotions** : arbres, murs, objets, vitesses.
3. **M3 – Niveaux et écrans** : accueil, fin de niveau, 3 niveaux.
4. **M4 – Finition** : sauvegarde, pause, sons, animations supplémentaires.

Les sprites en pixel art sont présents dès M1 (versions simples), puis enrichis.

## 12. Décisions à valider
1. ~~Vue 2D de côté~~ : **validée par Christian.** Reste à confirmer : un seul geste, toucher l'écran pour sauter.
2. Trébuchement qui ralentit, sans système de vies ; défaite si la souris s'échappe.
3. Outils à usage unique, locomotions à durée limitée (valeurs du tableau 4.2).
4. Trois niveaux de longueur finie pour la v1, mode sans fin hors périmètre.
5. Les jalons M1 à M4 et l'ordre de livraison.
6. Pixel art : grille 16×16, palette réduite, sprites dessinés dans le code *(le style est décidé ; le détail technique est à confirmer)*.
