# Spécification technique – Chat & Souris

| | |
|---|---|
| Statut | **Brouillon à valider** |
| Date | 2026-10-08 |
| Basé sur | `00-expression-de-besoin.md` et `01-prd.md` (validés) |
| Étape suivante | Plan (`03-plan.md`), puis code, après validation de ce document |

> Les valeurs chiffrées sont des **valeurs initiales** : elles sont regroupées dans un seul fichier de paramètres pour être ajustées après les essais sur la tablette. Les points qui s'écartent du PRD ou le précisent sont listés en section 12.

## 1. Choix techniques
| Sujet | Choix | Raison |
|---|---|---|
| Langage / UI | Kotlin + Jetpack Compose (Canvas) | Même socle que Morpion et BlocMon, testable par la CI |
| Version Android | minSdk 24, compileSdk/targetSdk 34 | Identique aux projets existants |
| Appareil | Tablette, **paysage fixe**, plein écran | Décision validée |
| Moteur de jeu | Aucun : boucle maison | Jeu 2D simple, pas de dépendance supplémentaire |
| Dépendances | Compose uniquement (BOM 2024.09.00) | Pas de bibliothèque externe |
| Sauvegarde | `SharedPreferences` | Suffisant pour la progression et les temps |
| Build / CI | Gradle 8.9, GitHub Actions (tests + APK), comme BlocMon | Cohérence |

Dossier du projet : `chatsouris/`, paquet `com.example.chatsouris`, nom affiché « Chat & Souris ».

## 2. Architecture
```
chatsouris/app/src/main/java/com/example/chatsouris/
  game/        Kotlin pur, sans Android, entièrement testé
    Params.kt        valeurs d'équilibrage
    Model.kt         types : Cat, Mouse, Obstacle, Item, Squirrel, Projectile, GameState
    Step.kt          step(state, input, dt) : l'avancée d'un pas de temps
    Collisions.kt    tests de recouvrement
    LevelDef.kt      définition des 3 niveaux
    LevelGenerator.kt génération déterministe d'un niveau à partir d'une graine
  render/      dessin Canvas, sprites, parallaxe
    Palette.kt, Sprites.kt, SpriteBitmaps.kt, GameRenderer.kt
  ui/          écrans Compose et boucle
    GameHost.kt (boucle), HomeScreen.kt, PlayScreen.kt, EndScreen.kt
  data/        Progress.kt (SharedPreferences)
  MainActivity.kt
```
Règles (identiques aux autres projets) : `game/` ne dépend ni d'Android ni de Compose ; `render/` et `ui/` n'implémentent aucune règle de jeu ; le hasard n'existe que dans le générateur de niveau, via une graine.

## 3. Boucle de jeu
- **Pas fixe** de 1/60 s. Un accumulateur consomme le temps réel écoulé (plafonné à 0,1 s pour éviter les sauts après une pause) et appelle `step` autant de fois que nécessaire.
- Pilotage par `withFrameNanos` dans un `LaunchedEffect`.
- `fun step(state: GameState, input: Input, dt: Float): GameState` est **pure et déterministe** : même état + mêmes entrées = même résultat. C'est ce qui rend le jeu testable sans appareil.
- `Input` : `jump: Boolean` (un toucher). La pause est gérée par l'écran, pas par `step`.
- Un toucher est mémorisé **0,1 s** (tampon de saut) pour pardonner les appuis légèrement en avance.

## 4. Repère et dimensions
- **Résolution virtuelle 320 × 180** px (16:9), pixels logiques.
- Agrandissement par un **facteur entier** (`floor(min(largeur/320, hauteur/180))`, au moins 1), centré, bandes noires si besoin. Pixels nets, sans lissage.
- Axe X vers la droite en unités du monde ; Y mesuré **vers le haut depuis le sol** (le sol est y = 0). Le sol est dessiné à 148 px du haut de l'écran virtuel.
- Le chat est affiché à x = 72 ; la caméra suit : `cameraX = chat.x − 72`.
- Sprites en 16×16 (obstacles hauts : 16×56 en empilant des tuiles).

## 5. Modèle de données
```kotlin
enum class ObstacleKind { BARRIER, HOLE, LAVA, TREE, WALL }
enum class ToolKind { CHAINSAW, HAMMER }
enum class LocomotionKind { SKATEBOARD, ROLLERS, BIKE }
enum class ItemKind { CHAINSAW, HAMMER, SKATEBOARD, ROLLERS, BIKE, SHIELD }

data class Cat(x, y, vy, onGround, stumbleLeft, invulnerableLeft,
               tools: Set<ToolKind>, locomotion: LocomotionKind?, locomotionLeft,
               shieldLeft, jumpBufferLeft)
data class Obstacle(kind, x, width, height, destroyed)   // y = 0
data class Item(kind, x, y, collected)
data class Squirrel(x, state, telegraphLeft)             // perché, lance vers la gauche
data class Projectile(x, y)                              // vole vers la gauche
data class GameState(level: LevelLayout, cat, mouseX, obstacles, items, squirrels,
                     projectiles, time, status: Playing|Won|Lost)
```
Tout est immuable (`copy`) ; moins de 40 entités actives à l'écran, donc coût négligeable.

## 6. Règles de jeu
### 6.1 Mouvement du chat
- Vitesse horizontale : `vitesseBase × multiplicateurLocomotion × multiplicateurTrébuchement`.
- Le chat avance toujours ; il ne s'arrête jamais.
- **Saut** : si `onGround` et (toucher ou tampon actif) → `vy = JUMP_V`. Pas de double saut. Gravité constante ; atterrissage quand `y ≤ 0`.
- Hauteur de saut ≈ 43,6 px, durée ≈ 0,62 s.

### 6.2 Souris, écart, victoire, défaite
- `gap = mouse.x − cat.x`.
- La souris avance à `mouseSpeed + mouseAccel × temps` (valeurs par niveau), mais **ne dépasse jamais la fin de piste** : `mouse.x = min(mouse.x, level.length)`.
- **Victoire** : `gap ≤ 0`. Comme la souris est bloquée en bout de piste et que le chat avance toujours, un niveau se termine forcément si le joueur n'est pas distancé.
- **Défaite** : `gap ≥ maxGap`.
- Vitesse de la souris proche de celle du chat : sans objet, l'écart reste stable ; les locomotions le réduisent, les trébuchements l'augmentent.

### 6.3 Obstacles
| Obstacle | Largeur × hauteur | Test de collision | Réponse |
|---|---|---|---|
| Barrière | 12 × 16 | Recouvrement du chat (10×12) avec le rectangle | Trébuchement si touché |
| Trou | 28 × 0 | Chat au sol dont plus de 4 px du corps est au-dessus du trou | Trébuchement |
| Lave | 24 × 4 | Recouvrement | Trébuchement |
| Arbre | 16 × 56 | Recouvrement | Si le chat a la tronçonneuse : obstacle détruit, outil consommé ; sinon trébuchement |
| Mur | 16 × 56 | Recouvrement | Idem avec le marteau |

Arbres et murs dépassent la hauteur de saut : ils ne se franchissent pas en sautant.

### 6.4 Trébuchement
- Pendant `STUMBLE_TIME` (0,8 s), la vitesse du chat est multipliée par 0,4.
- Puis `INVULN_TIME` (1,0 s au total, comptée depuis le choc) : plus aucun choc ne compte, ce qui évite les enchaînements injustes. Pendant cette fenêtre le chat traverse l'obstacle.
- Un trébuchement n'enlève ni outil ni locomotion.

### 6.5 Objets
| Objet | Effet | Valeurs initiales |
|---|---|---|
| Tronçonneuse / Marteau | Ajoute l'outil (au plus un de chaque) ; consommé au premier arbre / mur rencontré | 1 usage |
| Skateboard | Multiplicateur de vitesse | ×1,3 pendant 8 s |
| Rollers | idem | ×1,5 pendant 6 s |
| Vélo | idem | ×1,8 pendant 5 s |
| Bouclier | Absorbe le prochain projectile | 1 projectile ou 10 s sans impact |

- Ramasser une locomotion remplace la précédente et relance son compteur.
- Ramasser un outil déjà possédé ne change rien (l'objet est quand même consommé).
- Ramassage : recouvrement du chat avec l'objet (aussi en l'air).

### 6.6 Écureuils et projectiles
- Un écureuil se déclenche quand le chat est à moins de 200 px devant lui. Il **prévient** pendant 0,6 s (image d'alerte), puis lance une **noix** vers la gauche à 160 px/s, à hauteur basse (bas de la noix à 2 px du sol, 6 px de haut, 8 de large).
- Un écureuil lance une seule noix.
- Le chat saute pour l'éviter, ou le bouclier l'absorbe (la noix disparaît, le bouclier aussi). Sans bouclier, une noix qui touche déclenche un trébuchement. Une noix qui sort de l'écran à gauche disparaît.

## 7. Niveaux
```kotlin
data class LevelDef(id, length, seed, catBaseSpeed, mouseSpeed, mouseAccel,
                    initialGap, maxGap, obstacleSpacing, itemFrequency,
                    allowedObstacles, allowedItems, squirrelCount)
```
| | Niveau 1 | Niveau 2 | Niveau 3 |
|---|---|---|---|
| Longueur (px) | 4 000 | 6 000 | 8 000 |
| Vitesse de base du chat | 100 px/s | 100 | 100 |
| Vitesse de la souris | 98 px/s | 100 | 102 |
| Accélération de la souris | 0 | 0,2 px/s² | 0,3 px/s² |
| Écart initial / maximal | 100 / 240 | 100 / 240 | 100 / 240 |
| Espacement mini entre obstacles | 160 px | 150 px | 140 px |
| Obstacles | barrière, trou | + lave, arbre, mur | tous |
| Objets | skateboard | + tronçonneuse, marteau, rollers, bouclier | tous (+ vélo) |
| Écureuils | 0 | 3 | 8 |

Durée indicative d'un niveau sans accident : environ 40 s, 60 s et 80 s.

### 7.1 Génération
`LevelGenerator.generate(def): LevelLayout` est **déterministe** (même `LevelDef` → même niveau), et respecte ces invariants, **tous testés** :
1. Aucun obstacle, objet ni écureuil avant x = 300 (zone de départ vide).
2. Distance entre deux obstacles consécutifs ≥ `obstacleSpacing`.
3. **Solvabilité** : pour chaque arbre (resp. mur), au moins un objet tronçonneuse (resp. marteau) est placé avant lui, après l'obstacle équivalent précédent, à moins de 600 px.
4. Aucun objet à l'intérieur d'un obstacle.
5. Tous les éléments sont dans `[300, length − 100]`.
6. Un écureuil n'est jamais placé à moins de 250 px d'un obstacle au sol (lisibilité).

## 8. Rendu
- **Sprites** : tableaux de chaînes de caractères, un caractère par pixel, associé à une couleur d'une **palette de 16 couleurs** (`Palette.kt`). Exemple : `".kk..kk."`.
- Au démarrage, chaque sprite est converti **une seule fois** en `ImageBitmap` ; le dessin utilise `drawImage` avec `FilterQuality.None`.
- Sprites prévus : chat (course ×2, saut, trébuchement), souris (course ×2), barrière, trou (tuiles du sol), lave, arbre, mur, noix, écureuil (repos, alerte, lancer), 6 objets, bouclier autour du chat, tuiles du sol, nuages et collines.
- **Parallaxe** : ciel fixe, fond lointain à 25 % de la vitesse, fond proche à 50 %, sol à 100 %.
- Animation par alternance d'images toutes les 0,12 s.
- Interface en jeu (écart, outil, locomotion, bouclier, pause) dessinée en Compose par-dessus le Canvas.

## 9. Écrans et navigation
`Accueil → Jeu → Fin de niveau` ; la pause se superpose au jeu.
- **Accueil** : titre, un bouton par niveau (verrouillé tant que le précédent n'est pas gagné).
- **Jeu** : toucher n'importe où = sauter ; bouton pause en haut à droite (ce toucher-là ne fait pas sauter).
- **Fin de niveau** : victoire (temps, meilleur temps, « Niveau suivant », « Rejouer ») ou défaite (« Rejouer »), plus « Accueil ».
- Le jeu se met en pause automatiquement quand l'application passe en arrière-plan.
- Plein écran immersif, orientation `sensorLandscape`, écran maintenu allumé.

## 10. Sauvegarde
`Progress` (via `SharedPreferences`) conserve : niveaux gagnés et meilleur temps par niveau. Lecture au lancement, écriture à la victoire. Sans donnée, le niveau 1 est ouvert. Une valeur illisible est ignorée (retour aux valeurs par défaut).

## 11. Stratégie de test
**Automatique (JUnit, sur `game/`, exécuté par la CI)**
| Exigence | Tests |
|---|---|
| US1 saut | saut au sol, pas de double saut, hauteur et durée, tampon de saut |
| US2 défilement / vitesse | vitesse selon la locomotion ; le chat n'arrête jamais d'avancer |
| US3 obstacles sautables | barrière, trou et lave : franchis en sautant, trébuchement sinon ; fenêtre d'invulnérabilité |
| US4 / US5 outils | arbre / mur : détruit avec l'outil (outil consommé), trébuchement sans |
| US6 locomotions | multiplicateur, durée, remplacement |
| US7 écart | `gap` calculé et mis à jour |
| US8 / US9 fin | victoire si `gap ≤ 0`, défaite si `gap ≥ maxGap` ; la souris est bloquée en bout de piste ; tout niveau sans accident se termine par une victoire |
| US10 niveaux | définitions cohérentes ; génération déterministe ; invariants 1 à 6 de la section 7.1 pour les 3 niveaux |
| US11 sauvegarde | écriture / lecture de `Progress` (avec un faux stockage) |
| US13 bouclier / projectiles | noix touche → trébuchement ; bouclier absorbe et disparaît ; expiration à 10 s ; saut évite la noix |
| **Simulation complète** | un « robot » qui saute avant chaque obstacle gagne les 3 niveaux (le niveau est faisable) |

**Manuel sur la tablette** (checklist à chaque jalon) : fluidité, sensation du saut, lisibilité des sprites, taille des zones tactiles, pause, retour d'application, absence de plantage sur 10 parties.

**Non testé automatiquement** : le dessin et les écrans Compose. La CI vérifie qu'ils compilent.

## 12. Écarts et précisions par rapport au PRD (à valider)
1. **Trou** : le PRD indiquait « chute puis retour sur le bord ». La spécification retient un **trébuchement identique aux autres obstacles**, sans repositionnement, pour rester simple et prévisible. Le ralentissement fait déjà perdre du terrain.
2. **Fin de niveau** : la souris est **bloquée en bout de piste**. C'est ce qui garantit qu'un niveau de longueur finie se termine par la capture.
3. **Défaite** : l'écart maximal est de 240 px (la souris reste visible à l'écran jusqu'à la défaite).
4. **Fenêtre d'invulnérabilité** de 1 s après un trébuchement (non prévue au PRD, ajoutée pour l'équité).
5. **Vitesse de la souris** : très proche de celle du chat (98 à 102 px/s) pour que les objets et les trébuchements décident de la partie.
6. **Pause automatique** quand l'app passe en arrière-plan.
7. **Niveaux verrouillés** : le niveau suivant s'ouvre après une victoire.

## 13. Hors périmètre de cette spécification
Sons et musique (jalon M4, spécifiés à ce moment-là), mode sans fin, projectiles en hauteur, nouveaux objets.
