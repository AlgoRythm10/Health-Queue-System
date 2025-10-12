# Healthcare Queue & Appointment Management System

A comprehensive healthcare management system that streamlines patient queues and appointment scheduling for healthcare facilities.

## 🏗️ Project Structure

```
├── backend/          # Spring Boot API (Java 17)
│   ├── src/
│   ├── pom.xml
│   └── mvnw
├── frontend/         # React Web Application
│   ├── src/
│   ├── public/
│   └── package.json
├── .gitignore       # Global gitignore
└── README.md        # This file
```

## 🚀 Technologies Used

### Backend
- **Java 17** - Modern Java version
- **Spring Boot 3.5.5** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data MongoDB** - Database operations
- **MySQL Connector** - Database connectivity
- **Lombok** - Reduces boilerplate code
- **Maven** - Build tool

### Frontend
- **React 19.2.0** - Modern UI library
- **React Router DOM** - Client-side routing
- **Axios** - HTTP requests
- **Tailwind CSS** - Utility-first CSS framework
- **PostCSS** - CSS processing

## 🛠️ Setup Instructions

### Prerequisites
- Java 17+
- Node.js 16+
- MongoDB or MySQL database
- Maven (included via wrapper)

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Install dependencies and run:
   ```bash
   ./mvnw spring-boot:run
   ```

3. The API will be available at `http://localhost:8080`

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. The web app will be available at `http://localhost:3000`

## 📝 Features

- **Patient Queue Management** - Real-time queue tracking
- **Appointment Scheduling** - Efficient appointment booking
- **User Authentication** - Secure login system
- **Role-based Access** - Different access levels for patients, doctors, and admins
- **Real-time Updates** - Live queue status updates

## 🔧 Development

### Running Tests

**Backend:**
```bash
cd backend
./mvnw test
```

**Frontend:**
```bash
cd frontend
npm test
```

### Building for Production

**Backend:**
```bash
cd backend
./mvnw clean package
```

**Frontend:**
```bash
cd frontend
npm run build
```

## 📋 API Documentation

The backend provides RESTful APIs for queue management, appointment scheduling, and user authentication. API documentation is available when running the backend server.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 🔗 Links

- [Backend Documentation](./backend/README.md)
- [Frontend Documentation](./frontend/README.md)

