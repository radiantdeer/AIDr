<h1 align="center"><i>AIDr</i><br><i>Healthcare Chatbot</i></h1>

## How-To
1. Install Android Studio. For how to install it, find it [here](https://developer.android.com/studio/)
2. When running Android Studio for the first time, there will be a prompt to configure your Android Studio installation. Just follow and use the recommended settings.
3. Clone this repository, and open it from Android Studio.
4. *Gradle* will start "compiling" things, this first run of *Gradle* will take a while.
If *Gradle* encounters an error, check your internet connection, as *Gradle* needs to download libraries needed for this app.
5. Develop & build!

## Modules

This list is subject to change, as files may grow/shrink.

|Name                 |Related XML (layout) file|Description           |
|---------------------|-------------------------|----------------------|
|AIDrChat.java        |activity_aidr_chat       |The main activity     |
|ReminderFragment.java|reminder                 |The main reminder tab |
|ChatFragment.java    |chat                     |The main chat tab     |
|TipsFragment.java    |tips                     |The main tips tab     |
|AddReminder.java     |activity_add_reminder, calendar_picker    |View & controller for adding reminders|
|ChatActivity.java    |activity_chat            |View & controller for the chatting feature| 

## What Works & What Doesn't Work

### Currently Working
- Chatting : For now, app will "mirror" your message
- Calendar & time picker for add reminder

### Incoming
- Speech-to-text, and *vice versa*
- "Dummy" file upload
- Chat history : currently chats are gone if the back button is pressed/phone is rotated/app is closed
- All of planned chatting functionalities : diagnosing, disease & drug explainer, hospital location
- Reminder lists
- Save reminders
- Settings (?)

## Libraries Used
- [stfalcon's ChatKit](https://github.com/stfalcon-studio/ChatKit)
- [jackandphantom's CircularProgressbar](https://github.com/sparrow007/CircularProgressbar/blob/master/README.md)