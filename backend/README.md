# Cloud-Land-sample-backend

Web backend with custom sign-in, sign-up, forgot password, change details and change password screens, using App ID Cloud Directory APIs.

![Cloud-Land-login-screen](/backend/public/images/cloud_land_login_screen.png)

## Before running 

### Create Platform API key

1. In IBM Cloud go to **Manage**->**Security**->**Platform API Keys**
2. Click on create, set a name and description of the API key and click create. 
3. Take a note of the created API key.

### Create and configure App ID service

1. Create App ID service from the [IBM Cloud services catalog](https://console.bluemix.net/catalog/services/app-id?taxonomyNavigation=apps) and name it _CloudLandAppIDService_.
2. In the App ID dashboard click on **Cloud Directory** under **Identity Providers** and select the **Custom Landing pages** tab. 
3. Set the _URL for your custom email address verification page_ to 
_https://cloud-land-backend.mybluemix.net/ibm/cloud/appid/view/account_confirmed_.
4. Set the _URL for your custom reset password page_ to 
_https://cloud-land-backend.mybluemix.net/ibm/cloud/appid/view/reset_password_form_.

## Deploying the sample to IBM Cloud

1. Download the **backend** folder.
2. Open the [credentials.json](/backend/credentials.json) file and paste your platform API key.
3. Open terminal at the **backend** folder and run: _bx cf push_.
4. Open the browser and go to: _https://cloud-land-backend.mybluemix.net_


Note:
If your App ID service is NOT deployed on US region: 
1. Go to [manifest.yml](/backend/manifest.yml) and change the _domain_ property according to the region your App ID service is deployed.
2. Change the the **Custom Landing pages** accordingly.
    
