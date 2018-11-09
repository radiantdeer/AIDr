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
|Adapter/ReminderAdapter.java|item_card_reminder|Adapter for viewing reminders|
|Adapter/DiseaseMessageViewHolder.java|         |Adapter/controller for chat viewing a disease|
|Adapter/OutMessageViewHolder.java|             |Adapter/controller for outgoing chat (although it's "unused")|
|Model/Reminder.java  |                         |Data structure/representation of reminders|
|Model/Message.java   |                         |Data structure/representation of messages|
|Model/Author.java    |                         |Data structure/representation of author of messages|
|AddReminder.java     |activity_add_reminder2, calendar_picker|View & controller for adding reminders|
|AIDrChat.java        |activity_aidr_chat       |The main activity     |
|ChatActivity.java    |activity_chat, chat_bubble_*|View & controller for the chatting feature| 
|ChatFragment.java    |chat                     |The main chat tab     |
|DiseaseDB.java       |                         |Used as an "interface" to disease database|
|DiseaseExplainer.java|activity_disease_explainer|Used to show details about a disease|
|ReminderFragment.java|reminder                 |The main reminder tab |
|TipsFragment.java    |tips                     |The main tips tab     |



## What Works & What Doesn't Work

### Currently Working
- Chatting : system can show disease if user types in the disease. Other than that, the system just mirrors the message.
- Disease Explainer
- Chat history
- Reminders : able to list reminders made & create new reminders

### Incoming
- Speech-to-text, and *vice versa*
- "Dummy" file upload
- All of planned chatting functionalities : diagnosing, drug explainer, hospital location
- *Feedbacks* for saving reminders ("Reminders added successfully!", or something like that)
- Push-notify of reminders
- Settings (?)

## Libraries Used
- [stfalcon's ChatKit](https://github.com/stfalcon-studio/ChatKit)
- [jackandphantom's CircularProgressbar](https://github.com/sparrow007/CircularProgressbar/blob/master/README.md)