# Petal 🌸

Petal is a full-stack memory journaling application that allows users to capture, reflect upon, and revisit their life's moments. It comes with a modern native Android client built with Jetpack Compose and a robust backend built with Django and the Django REST Framework. 

With Petal, every memory is more than just text—it's an interactive experience tied to locations, moods, images, and audio.

---

## ✨ Features

- **Mood Tracking**: Log how you felt during the moment (Calm, Happy, Sad, Anxious, etc.).
- **Location Tagging**: Integrated with Google Maps API and Places API to automatically or manually log exactly where the memory happened.
- **Rich Media**:
  - Upload multiple images per memory.
  - Record or upload an audio note/diary entry.
- **Cloud Storage**: Seamless media streaming and storage with Cloudinary.
- **Organization**: Tag your memories, mark them as favorites, and filter them by month.
- **Secure Authentication**: Register and login securely using JWT tokens.

---

## 🛠 Tech Stack

### Frontend (Android app)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Navigation**: Voyager
- **Network calls**: Retrofit & Gson
- **Image Loading**: Coil
- **Concurrency**: Kotlin Coroutines
- **Maps & Location**: Google Maps SDK, Google Places API, Accompanist (Permissions)

### Backend (REST API)
- **Framework**: Django & Django REST Framework (DRF)
- **Database**: PostgreSQL
- **Authentication**: Simple JWT
- **Media Storage**: Cloudinary & django-cloudinary-storage
- **Media Processing**: Pillow (Images), Mutagen (Audio duration & metadata validation)

---

## 🚀 Getting Started

### Prerequisites
- [Python 3.10+](https://www.python.org/downloads/)
- [Android Studio (Latest)](https://developer.android.com/studio)
- A [Cloudinary Account](https://cloudinary.com/) (for media storage)
- A Google Maps/Places API Key
- PostgreSQL installed and running

### Backend Setup

1. **Navigate to the backend directory:**
   ```bash
   cd backend/memory_project
   ```

2. **Create and activate a virtual environment:**
   ```bash
   python -m venv venv
   # On Windows
   venv\Scripts\activate
   # On Unix/macOS
   source venv/bin/activate
   ```

3. **Install the dependencies:**
   ```bash
   pip install -r requirements.txt
   ```

4. **Environment Variables:**
   Create a `.env` file in the `memory_project` directory containing:
   ```env
   # Django
   DEBUG=True
   SECRET_KEY=your_django_secret_key
   
   # PostgreSQL
   DB_NAME=your_db_name
   DB_USER=your_db_user
   DB_PASSWORD=your_db_password
   DB_HOST=localhost
   DB_PORT=5432
   
   # Cloudinary
   CLOUDINARY_URL=cloudinary://<api_key>:<api_secret>@<cloud_name>
   ```

5. **Run Migrations:**
   ```bash
   python manage.py makemigrations
   python manage.py migrate
   ```

6. **Start the Development Server:**
   ```bash
   python manage.py runserver
   ```
   *The backend will be running at `http://127.0.0.1:8000/`.*

### Android Setup

1. Open Android Studio and select **Open an existing project**.
2. Navigate to and select the `android/Petal` folder.
3. Once the Gradle sync is complete, add your Google Maps API key.
4. Create a `local.properties` file in `android/Petal` (if it does not exist) and add your API key:
   ```properties
   MAPS_API_KEY=your_actual_google_maps_api_key_here
   ```
5. Run the application on an Emulator or a physical device.

---

## 📝 API Endpoints Summary

- `POST /register/` - Register a new user
- `POST /login/` - Login and receive JWT tokens
- `GET /profile/` - Get user profile details
- `GET /memories/` - List all user memories
- `POST /memories/` - Create a new memory
- `GET /memories/by-month/` - Group memories by month
- `GET /memories/<id>/` - Retrieve memory details
- `POST /memories/<id>/favorite/` - Mark a memory as favorite
- `POST /memories/<id>/images/` - Add images to an existing memory
- `POST /memories/<id>/audio/` - Upload an audio file for a memory

*(All memory endpoints are protected and require the `Authorization: Bearer <token>` header)*

---

## 🤝 Contributing
Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.
