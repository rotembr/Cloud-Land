# Cloud-Land-Android-application
Android application with custom sign-in, sign up, forgot password, change password and change details screens, support by Cloud Land backend.

![alt text](/Android-application/app/src/main/res/drawable/cloud_land_sign_up.png)

## Before running 
1. Follow the instruction for creating Cloud Land backend [Cloud Land backend](/backend/README.md).
2. In the CloudLandAppIDService dashboard go to the **service credentials** tab, in the **Action** column click on **View credentials**.
3. Copy the `tenantId` GUID number.
4. Paste the `tenantId` to the [credentials.xml](/Android-application/app/src/main/res/values/credentials.xml)
5. If your App ID service is not deployed on US region, go to [MainActivity.java](/Android-application/app/src/main/java/com/ibm/bluemix/appid/cloud/directory/android/sample/appid/MainActivity.java) and change the region according to the region your App ID service is deployed.
6. In the [MainActivity.java](/Android-application/app/src/main/java/com/ibm/bluemix/appid/cloud/directory/android/sample/appid/MainActivity.java) replace "Your-Cloud-Land-backend-url" with your Cloud land backend url.
7. Run the Cloud Land app.
