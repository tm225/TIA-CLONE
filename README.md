# TIA Clone

Squelette de projet Android pour une application de **clonage d'applications**
(permettre de lancer une 2e instance d'une app deja installee, avec ses
propres donnees et sa propre session reseau — ex. un 2e compte WhatsApp).

## Ce que fait ce squelette

- `MainActivity` liste les applications installees sur l'appareil
  (`PackageManager.getInstalledApplications`).
- Chaque ligne a un bouton "Cloner" (`onCloneRequested` dans `MainActivity.kt`) :
  c'est ici que doit se brancher le moteur de virtualisation (conteneur +
  classloader isole) qui cree la 2e instance.
- `requestLocationIfNeeded()` montre le pattern a suivre pour **chaque**
  permission sensible (GPS, camera, contacts...) : elle passe toujours par
  `ActivityCompat.requestPermissions`, donc par le dialogue systeme Android
  standard. Aucune permission n'est obtenue en contournant ce dialogue.

## Ce qui reste a faire

1. **Moteur de virtualisation** : ce squelette ne clone pas encore reellement
   une app. Il faut integrer ou adapter un moteur de sandboxing Android
   (point de depart courant : un fork de VirtualApp) qui :
   - charge l'APK cible dans un processus/conteneur isole,
   - redirige son stockage interne vers un dossier separe par clone,
   - relaie ses demandes de permission vers les dialogues systeme (jamais
     de contournement).
2. **Gestion reseau par instance** : donner a chaque clone ses propres
   cookies/sessions pour qu'il puisse se connecter a un compte different.
3. **Icone et style** : remplacer l'icone par defaut et adapter les couleurs
   dans `res/values/colors.xml`.

## Compiler

### En local
```bash
./gradlew assembleDebug
```
(Necessite d'ouvrir le projet une fois dans Android Studio pour qu'il genere
le dossier `gradle/wrapper` avec le jar du wrapper, absent de ce squelette.)

### Sur GitHub (automatique)
Le workflow `.github/workflows/build.yml` compile l'APK a chaque `push` sur
`main` et le publie comme artefact telechargeable depuis l'onglet **Actions**
du depot, sous le nom `tia-clone-debug-apk`.

## Limites a connaitre

Certaines apps (bancaires notamment) detectent et bloquent le clonage par
sandboxing — c'est une protection normale de leur part, pas un bug du projet.
