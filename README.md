# drive-sdk
Drive Client SDK

The pt.ist.drive.sdk.ClientFactory provides two implementations of the Drive 
Client, one based on local configuration files, and another to be configured 
directly with a Java API.

## ClientFactory.configurationDriveClient()

This method will provide a DriveClient instance that will lookup the following 
properties from your configuration.properties file.

```
drive.url: URL to your Drive installation
drive.client.refresh.token: Refresh token to use to get access tokens
drive.client.app.id: OAuth2 Client App ID
drive.client.app.user: Username of the user that registered the the OAuth2 Client App
```

## ClientFactory.driveClient()

This method will create a new DriveClient instance with the provided 
configuration properties passed as method arguments.
