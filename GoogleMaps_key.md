# Introduction #

How to get your own apiKey (to set on game.xml):


# Details #

do
`keytool -v -list -alias androiddebugkey -keystore ~/.android/debug.keystore -storepass android -keypass android`

Check if `~/.android/debug.keystore` is the right path to your keystore

Go there and generate your key: http://code.google.com/intl/fr-FR/android/add-ons/google-apis/maps-api-signup.html