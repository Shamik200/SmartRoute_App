# Geospatial Optimization
[![Python](https://img.shields.io/badge/Python-3.8%2B-blue.svg)](https://www.python.org/)
[![Flask](https://img.shields.io/badge/Flask-2.1%2B-orange.svg)](https://flask.palletsprojects.com/)
[![Geopy](https://img.shields.io/badge/Geopy-2.2%2B-blue.svg)](https://geopy.readthedocs.io/en/stable/)
![badge-Platform](https://img.shields.io/badge/Platform-Backend%2C%20Mapping%2C%20Visualization-brightgreen)

Geospatial Optimization is an advanced routing and clustering system designed to optimize delivery trips for a facility by clustering shipments and calculating optimal routes. Built with Python using Flask for backend development and Geopy for geospatial calculations, this system aims to enhance efficiency in logistics through advanced route optimization techniques. Additionally, it integrates with an Android app to display optimized routes using external intents to Google Maps for navigation.

## Key Features ✨
- Geodesic distance calculation between facility and shipments using **Geopy**.
- Clustering of shipments into optimized trips based on proximity and vehicle capacity.
- Visualization of optimized routes on a dynamic map using **Folium**.
- Integration of custom **CSS** for enhanced user experience in viewing multiple trip groups.
- **Android Integration**:
  - External intent to **Google Maps** for route navigation from facility to shipments.
  - Seamless synchronization between the backend and Android app for real-time trip updates.

<p align="center">
  <img width="30%" height="50%" src="https://github.com/Shamik200/SmartRoute/blob/main/Images/img1.jpg" />
  <img width="30%" height="50%" src="https://github.com/Shamik200/SmartRoute/blob/main/Images/img2.jpg" />
  <img width="30%" height="50%" src="https://github.com/Shamik200/SmartRoute/blob/main/Images/img3.jpg" />
  <img width="30%" height="50%" src="https://github.com/Shamik200/SmartRoute/blob/main/Images/img4.jpg" />
  <img width="30%" height="50%" src="https://github.com/Shamik200/SmartRoute/blob/main/Images/img5.jpg" />
</p>

## Project Layout Structure
- **Backend:**
  - Flask API to manage geospatial calculations, trip optimizations, and database interactions.
  - Geospatial services to calculate distances and optimize trip clustering.
- **Frontend:**
  - Map visualization with **Folium**.
  - Dynamic routing and trip display on a custom map with clustering results.
- **Trips:**
  - Each shipment is assigned to an optimal trip using clustering techniques based on polar angles.
  - Custom **CSS** for scrolling trip groups.
- **Android Integration:**
  - External intents to **Google Maps** for route navigation from the facility to each shipment.
  - Syncs with the Flask backend to display updated trips and navigation data.
  
## Backend Details 💻
### Geospatial Distance Calculation
- **Geopy** library is used for calculating **geodesic distances** between coordinates (facility and shipments).
- The backend API is designed to handle geospatial calculations and optimize trips based on distance.

### Flask Backend
- API endpoints handle data requests for optimized trip results, calculations, and map data.

### Deployment
- The application is hosted on **Render** for easy deployment of backend services.
- Supports dynamic scaling based on usage and is integrated with GitHub Actions for CI/CD pipelines.

## Visualization with Folium
- **Folium** is used to create an interactive map that displays the facility, shipments, and the optimized trip paths.
- Routes are visualized with **polylines**, and shipments are marked with random colors for differentiation.

### Map Customization
- Custom **CSS** is used to enhance the map control, making it scrollable to handle a large number of trip groups.

## Android Integration 🌍
- The **Android app** integrates with the Flask backend using **REST APIs** to fetch optimized trips and shipment details.
- External intents are used to open **Google Maps** for navigation:
  - The app sends the optimized routes directly to Google Maps to guide drivers from the facility to each shipment.
  - Custom buttons or actions allow users to select the best trip and open the route in Google Maps for navigation.
- The **Jetpack Compose** framework is used for building the UI of the Android app, making the interface modern and intuitive.
  
## Architecture 🏗️
- **Flask Backend** - Handles API requests and geospatial calculations.
- **Folium Frontend** - Renders optimized trips and clusters on a map.
- **Android Integration** - External intents for Google Maps navigation.

## Built With 🛠
- [Python](https://www.python.org/) - Primary language for backend development.
- [Flask](https://flask.palletsprojects.com/) - Lightweight backend framework.
- [Geopy](https://geopy.readthedocs.io/en/stable/) - Library for geodesic distance calculations.
- [Folium](https://python-visualization.github.io/folium/) - Map visualization library.
- [Render](https://render.com/) - Deployment platform for backend services.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI framework for Android.
- [Google Maps API](https://developers.google.com/maps/documentation/urls/get-started) - Used for external intent navigation.

## Testing 🧪
- Unit tests for backend API logic.
- Integration tests for trip optimization functionality.
- UI tests for Android app navigation and Google Maps intents.

<h2> 📬 Connect With Me </h2>

<div>
  <a href="https://www.linkedin.com/in/shamik-munjani/">
    <img src="https://www.vectorlogo.zone/logos/linkedin/linkedin-icon.svg" width="30px" alt="LinkedIn">
  </a>
  <span style="margin: 5px;"></span>
  <a href="mailto:shamikmunjani@gmail.com">
    <img src="https://www.vectorlogo.zone/logos/gmail/gmail-icon.svg" width="30px" alt="Email">
  </a>
</div>
<br>

⭐️ From [Shamik](https://github.com/Shamik200)
