# Cloud-Land-Android-application
Android application with custom sign-in, sign up, forgot password, change password and change details screens, support by Cloud Land backend.

![alt text](/Android-application/app/src/main/res/drawable/cloud_land_login.png)
![alt text](/Android-application/app/src/main/res/drawable/cloud_land_sign_up.png)

## Before running 
1. Follow the instruction for creating Cloud Land backend [Cloud Land backend](/backend/README.md).
2. In the CloudLandAppIDService dashboard go to the **service credentials** tab, in the **Action** column click on **View credentials**.
3. Copy the `tenantId` GUID number.
4. Paste the `tenantId` to the [credentials.xml](/Android-application/app/src/main/res/values/credentials.xml)
5. If your App ID service is not deployed on US region:
- Go to [MainActivity.java](/Android-application/app/src/main/java/com/ibm/bluemix/appid/cloud/directory/android/sample/appid/MainActivity.java) and change the region according to the region your App ID service is deployed.
- In the [MainActivity.java](/Android-application/app/src/main/java/com/ibm/bluemix/appid/cloud/directory/android/sample/appid/MainActivity.java) replace "CLOUD_LAND_BACKEND_URL" with your Cloud land backend url.
- In the [strings.xml](/Android-application/app/src/main/res/values/strings.xml) replace the 'site' property under 'asset_statements' with your Cloud land backend url.
6. Run the Cloud Land app.

Note: 
- When Clicking on a 'Verify' or a 'Reset' button on the mobile email client, 
if the Cloud land app is installed on the device then it will be launch and if the app is not installed then the web browser will be use.
  In order for it to work Chrome 59 and above is require and while the mobile app is in development, a Chrome flag, 'Experimental web platform features' need to be enabled.
It can be enabled from to the chrome://flags in the mobile browser.
