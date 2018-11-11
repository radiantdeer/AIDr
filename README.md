<h1 align="center"><i>AIDr</i><br><i>Healthcare Chatbot</i></h1>

If you're looking for prebuilt APKs, you can find it [here](https://github.com/antoniosetya/AIDr/releases)

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
|Adapter/DiseaseMessageViewHolder.java|         |Adapter/controller for chat viewing a disease|
|Adapter/LocationAdapter.java|                  |Adapter/controller for RecyclerView in chat_bubble_locations|
|Adapter/LocationMessageViewHolder.java|        |Adapter/controller for chat viewing locations of hospital|
|Adapter/OutMessageViewHolder.java|             |Adapter/controller for outgoing chat (although it's "unused")|
|Adapter/ReminderAdapter.java|item_card_reminder|Adapter for viewing reminders|
|Model/Author.java    |                         |Data structure/representation of author of messages|
|Model/Hospital.java  |                         |Data structure/representation of hospitals|
|Model/Message.java   |                         |Data structure/representation of messages|
|Model/Reminder.java  |                         |Data structure/representation of reminders|
|AddReminder.java     |activity_add_reminder2, calendar_picker|View & controller for adding reminders|
|AIDrChat.java        |activity_aidr_chat       |The main activity     |
|ChatActivity.java    |activity_chat, chat_bubble_*|View & controller for the chatting feature| 
|ChatFragment.java    |chat                     |The main chat tab     |
|DiseaseDB.java       |                         |Used as an "interface" to disease database|
|DiseaseExplainer.java|activity_disease_explainer|Used to show details about a disease|
|NotificationService.java|                      |Used to push notifications|
|ReminderDB.java      |                         |Used as an "interface" to reminder file & setting reminder notifications|
|ReminderFragment.java|reminder                 |The main reminder tab |
|TipsFragment.java    |tips                     |The main tips tab     |

## What Works & What Doesn't Work

### Currently Working
- Chatting : 
    - All hail the glorious speech-to-text!
    - Simple scenario of diagnosing disease.
    - System can show disease if user ask information of a disease.
    - System can show drugs for a specific disease. 
    - System can also show (dummy) nearest hospital. 
    - Other than that, the system just mirrors the message.
- Disease Explainer
- Chat history
- Reminders : able to list reminders made & create new reminders
- Limited push-notification of reminders : only works when app is opened. If app is on background, Android will defer notifications pushed by this app. Also, the time it fires is also not exact, it might miss by around 45 seconds.

### Incoming
- Text-to-speech
- "Dummy" file upload
- *Feedbacks* for saving reminders ("Reminders added successfully!", or something like that)
- Settings (?)

## Libraries Used
- [stfalcon's ChatKit](https://github.com/stfalcon-studio/ChatKit)
- [jackandphantom's CircularProgressbar](https://github.com/sparrow007/CircularProgressbar/blob/master/README.md)