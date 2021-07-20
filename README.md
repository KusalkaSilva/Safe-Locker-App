# Safe-Locker-App
# A university group project

A mobile application for securely hiding photos and videos

Functions -

. Solve privacy issues of users by securely hiding confidential data

. Can lock and manage private photos and videos 

. Space encryption: access app with personal password

. Intruder access attempts are notified to the user 

. Backup photos and videos to a personal cloud space

Safe Locker is a practical implementation of securely hiding photos and videos. Through Safe Locker we have provided desired privacy for the users. When the user launches the Safe Locker for the first time, user will be asked to create a 4 digit pin code, along with that created a set of questions will be displayed to the user, from which set of 3 random questions must be answered which then act as the recovery option in case the user forgets the pin code. After entering the valid pin code user will be granted access to Safe Locker. Fingerprint id also act as an alternative way of accessing the Safe Locker. Once recognized by the app the user will be directed to the home page. Within the home page user is provided with multiple options.
     Photos
     Videos
     Break in Alerts
     Settings
By selecting on each option the user will be directed to its respective page.
Within the Photos option, user will be facilitated to import photos from the phone gallery as well as export photos back to the phone gallery. When the user clicks on a photo, the photo is viewed in full screen. Upon long press select option is activated. User is capable of deleting photos which will be moved to a trash folder, which results a temporary deletion. Photos can be permanently removed from the Safe Locker by cleaning the trash folder. Photo recovery option is also provided within the trash folder. Selected back up of photos is managed within the Firebase.
Same scenario works with the Videos option.
If someone enters an invalid pin code 3 times the app detects him as an intruder and captures a photo. Then thorough the Break in Alert option in the home view the user is notified about the break in attempt along with the picture taken.
In Settings the user is provided with two options;
     Change Password
     Recovery Option
In the Change Password option user can update its existing password.
In the Recovery Option the user is allowed to update the user-defined questions and answers, which will act as the only recovery mechanism in case the user forgets its password.
