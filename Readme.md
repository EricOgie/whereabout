# Whereabout

**Whereabout** is a safety-focused mobile app designed to help users identify their current location, monitor the whereabouts of connected devices in real-time, and trigger emergency alerts when needed. The app allows secure connections via QR codes or PINs and supports live tracking and emergency alerts, including messages, images, or voice notes, to assist in critical situations.

## Features
- **Real-Time Location Sharing**: Track your location and connected devices in real time.
- **Secure Connections**: Use dynamic QR codes or PINs to link with other trusted devices.
- **Emergency Alerts**: Automatically share location updates and send detailed alerts with messages, images, and voice notes during emergencies.

---

## Getting Started with Local Development

Follow these steps to set up the "Whereabout" app for local development.

### Prerequisites
- **Android Studio**: Download and install [Android Studio](https://developer.android.com/studio).
- **Firebase Project**: Set up a Firebase project and configure Google Maps for Firebase (details below).
- **Google Maps API Key**: Obtain an API key for Google Maps SDK.

### Project Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/whereabout.git
   cd whereabout
   ```

2. ***Set up Firebase Project**:
- Go to the Firebase Console and create a new project.
- Register your Android app within the project using your app’s package name, e.g., com.tees.xrtar.whereabout.
- Download the google-services.json file provided by Firebase and place it in the app/ directory.

2. ***Running the App**:
- Open the project in Android Studio.
- Ensure your Android device or emulator is running.
- Build and run the app by selecting Run > Run 'app'.