# Cloud-Land-sample-backend

Web backend with custom sign-in, sign-up, forgot password, change details and change password screens, using App ID Cloud Directory APIs.

![Cloud-Land-login-screen](/backend/public/images/cloud_land_login_screen.png)

## Before running 

### Create and configure App ID service

1. Create App ID service from the [IBM Cloud services catalog](https://console.bluemix.net/catalog/services/app-id?taxonomyNavigation=apps) and name it _CloudLandAppIDService_.
2. In the App ID dashboard click on **Cloud Directory** under **Identity Providers** and select the **Custom Landing pages** tab. 
3. Set the _URL for your custom email address verification page_ to
_https://cloud-land-backend-< YOUR NAME >.mybluemix.net/ibm/cloud/appid/view/account_confirmed_
after replacing "< YOUR NAME >" with your name.
4. Set the _URL for your custom reset password page_ to
_https://cloud-land-backend-< YOUR NAME >.mybluemix.net/ibm/cloud/appid/view/reset_password_form_
after replacing "< YOUR NAME >" with your name.

## Deploying the sample to IBM Cloud

1. Download the **backend** folder.
2. Replace the "YOUR SPACE" with your space in the following command and run it: 
bx resource service-alias-create "_CloudLandAppIDService_" --instance-name "_CloudLandAppIDService_" -s "YOUR SPACE" 
3. Go to [manifest.yml](/backend/manifest.yml) and replace "< YOUR NAME >" with your name in the _host_ property.
4. Open terminal at the **backend** folder and run: _bx app push_.
5. Open the browser and go to: _https://cloud-land-backend-< YOUR NAME >.mybluemix.net_


Note:
If your App ID service is NOT deployed on US region: 
1. Go to [manifest.yml](/backend/manifest.yml) and change the _domain_ property according to the region your App ID service is deployed.
2. Change the the **Custom Landing pages** accordingly.
    
