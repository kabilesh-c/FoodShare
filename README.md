# 🍲 FoodShare - Fighting Hunger Through Technology

<div align="center">

![FoodShare Banner](https://img.shields.io/badge/FoodShare-Combat%20Food%20Waste-brightgreen?style=for-the-badge)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen?style=flat-square&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)

**A modern, full-stack web application bridging the gap between food surplus and food insecurity**

[Live Demo](#) • [Features](#-key-features) • [Installation](#-installation) • [Contact](#-developer-contact)

</div>

---

## 🌍 The Problem We're Solving

### **The Global Food Waste Crisis**

Every year, our world faces a devastating paradox:

- 📊 **1.3 Billion Tons** of food is wasted globally every year
- 💰 **$1 Trillion** worth of food ends up in landfills
- 😢 **828 Million People** suffer from hunger worldwide
- 🏪 **30-40%** of food produced in India never reaches consumers
- 🍽️ Restaurants, hotels, and events discard perfectly edible food daily
- 🌾 Meanwhile, millions go to bed hungry every night

### **The Disconnect**

The real tragedy isn't just the waste—it's the **disconnect between surplus and need**:

- **Donors** (restaurants, hotels, events, individuals) have excess food but no efficient way to donate
- **Receivers** (NGOs, shelters, food banks, individuals in need) struggle to find reliable food sources
- **Time-sensitive nature** of food donations makes coordination critical
- **Lack of transparency** prevents donors from tracking their impact
- **Trust and safety concerns** discourage participation

### **Our Impact**

FoodShare bridges this critical gap by creating a **trusted, efficient, and transparent platform** that:

✅ **Reduces food waste** by connecting donors with receivers in real-time  
✅ **Fights hunger** by ensuring surplus food reaches those who need it most  
✅ **Builds community** by fostering a culture of sharing and compassion  
✅ **Provides transparency** through analytics and impact tracking  
✅ **Saves resources** by preventing edible food from reaching landfills  
✅ **Environmental impact** - reducing food waste means less methane emissions from landfills

---

## 🎯 Why This Solution Matters

### **For Society**
- **Social Responsibility**: Enables individuals and businesses to contribute to solving hunger
- **Community Building**: Creates connections between different sectors of society
- **Awareness**: Raises consciousness about food waste and hunger issues

### **For Donors**
- **Easy to Use**: Simple interface to list surplus food in minutes
- **Tax Benefits**: Documentation for CSR and donation records
- **Brand Image**: Demonstrate social responsibility and community involvement
- **Zero Waste Goals**: Help businesses achieve sustainability targets

### **For Receivers**
- **Reliable Access**: Find available food donations in real-time
- **Transparency**: Know exactly what's available and where
- **Dignity**: Access food with respect and ease
- **Community Support**: Connect with other organizations and individuals

### **For the Environment**
- **Reduced Landfill Waste**: Every donation prevents food from rotting in landfills
- **Lower Carbon Footprint**: Reduced methane emissions from decomposing food
- **Resource Conservation**: Saves water, energy, and resources used in food production

---

## ✨ Key Features

### 🎁 **For Donors**
- **Quick Donation Listing**: Add food donations with photos, quantity, and expiry details
- **Category Management**: Classify food as Veg 🥗, Non-Veg 🍗, or Vegan 🌱
- **Real-time Dashboard**: Track your donations, impact metrics, and success rate
- **Edit & Delete**: Full control over your donation listings
- **Analytics**: Visualize your contribution with interactive charts
  - Total donations made
  - Food saved (in kg)
  - People fed estimate
  - Success rate percentage
- **Image Upload**: Showcase food quality with photo uploads
- **Expiry Tracking**: Automatic highlighting of time-sensitive donations

### 🤝 **For Receivers**
- **Browse Donations**: View all available food in your area
- **Smart Filtering**: Filter by food type, location, and quantity
- **Claim System**: Request donations with a single click
- **History Tracking**: Keep track of all your claims and requests
- **City-based Matching**: Automatic matching with nearby donations
- **Real-time Updates**: See newly added donations instantly
- **Impact Dashboard**: Track how much food you've received

### 👨‍💼 **For Administrators**
- **User Management**: Monitor and manage all donors and receivers
- **System Reports**: Comprehensive analytics on platform usage
- **Top Donors**: Recognize and highlight active contributors
- **Food Saved Metrics**: Track total impact across the platform
- **CSV Exports**: Download reports for analysis and record-keeping
- **User Verification**: Ensure platform integrity and safety

### 🎨 **User Experience**
- **Modern Glassmorphism UI**: Beautiful, contemporary design with smooth animations
- **Responsive Design**: Perfect experience on mobile, tablet, and desktop
- **Dark Mode**: Eye-friendly interface for any time of day
- **Custom Form Controls**: Professionally styled radio buttons, checkboxes, and inputs
- **Real-time Statistics**: All dashboard data pulled from live database
- **Interactive Charts**: Visual representation using Chart.js
- **Intuitive Navigation**: Easy-to-use interface for all user levels
- **Fast Performance**: Optimized loading and smooth transitions

### 🔐 **Security & Reliability**
- **Spring Security**: Industry-standard authentication and authorization
- **Role-based Access**: Separate portals for Donors, Receivers, and Admins
- **Session Management**: Secure user sessions with automatic timeout
- **Password Encryption**: BCrypt hashing for all passwords
- **SQL Injection Protection**: Parameterized queries via Spring Data JPA
- **CSRF Protection**: Built-in cross-site request forgery prevention
- **Cloud Database**: Supabase PostgreSQL with SSL encryption
- **Data Validation**: Server-side validation for all user inputs

---

## 🛠️ Technology Stack

### **Backend**
- **Java 17**: Modern, robust programming language
- **Spring Boot 3.2.4**: Enterprise-grade application framework
- **Spring Security 6.2.3**: Comprehensive security solution
- **Spring Data JPA**: Simplified database operations
- **Hibernate 6.4.4**: Powerful ORM framework

### **Frontend**
- **Thymeleaf 3.1.2**: Server-side template engine
- **HTML5 & CSS3**: Modern web standards
- **JavaScript**: Client-side interactivity
- **Bootstrap 5.3.0**: Responsive CSS framework
- **Chart.js 4.4.0**: Interactive data visualization
- **Font Awesome 6.5.1**: Professional icon library

### **Database**
- **PostgreSQL**: Reliable, ACID-compliant relational database
- **Supabase**: Cloud-hosted database with SSL security
- **HikariCP**: High-performance connection pooling

### **Build & Deployment**
- **Maven 3.9+**: Dependency management and build automation
- **Railway/Render**: Cloud deployment platforms
- **Docker**: Containerization support
- **Git**: Version control

---

## 📊 Project Statistics

```
📁 32 Java Classes
🎨 31 HTML Templates  
🚀 3 User Roles (Donor, Receiver, Admin)
🗄️ 6 Database Entities
🔒 Spring Security Integration
📈 Real-time Analytics Dashboard
🌐 Cloud-based PostgreSQL Database
```

---

## 🚀 Installation

### **Prerequisites**
- Java JDK 17 or higher
- Maven 3.6 or higher
- Internet connection (for Supabase database)

### **Quick Start**

```bash
# Clone the repository
git clone https://github.com/yourusername/FoodShare.git
cd FoodShare

# Build the project
mvn clean package -DskipTests

# Run the application
mvn spring-boot:run

# Access at http://localhost:8083
```

### **Demo Accounts**

| Role | Email | Password |
|------|-------|----------|
| 👨‍💼 Admin | admin@gmail.com | password123 |
| 🎁 Donor | donor@gmail.com | password123 |
| 🤝 Receiver | receiver@gmail.com | password123 |

### **Detailed Installation**

For comprehensive installation instructions including IDE setup, troubleshooting, and deployment guides, see [INSTALLATION.md](INSTALLATION.md).

---

## 🌐 Deployment

### **Deploy to Railway** (Recommended - Free Tier)

1. Push code to GitHub
2. Visit [railway.app](https://railway.app)
3. Connect your repository
4. Railway auto-deploys your application
5. Get your live URL instantly!

**Other Deployment Options:**
- Render (750 hours/month free)
- Heroku (with GitHub Student Pack)
- Google Cloud Platform (free tier)
- Oracle Cloud (always free tier)
- Azure (student credits)

All necessary configuration files (`Procfile`, `railway.json`) are included.

---

## 📸 Screenshots

### Donor Dashboard
*Real-time analytics showing total donations, food saved, people fed, and success rate*

### Add Donation Form
*Custom-styled form with image upload, food categorization, and expiry tracking*

### Receiver Portal
*Browse and claim available food donations in your area*

### Admin Reports
*Comprehensive system analytics and user management*

---

## 🎯 Use Cases

### **Restaurants & Hotels**
- Donate surplus food from buffets and events
- Track CSR impact and sustainability metrics
- Build positive brand reputation

### **Corporate Events & Weddings**
- Share leftover food from large gatherings
- Prevent wastage from catering surplus
- Support local communities

### **Individual Donors**
- Share home-cooked food or excess groceries
- Contribute during festivals and celebrations
- Make a difference in someone's life

### **NGOs & Charities**
- Reliable source for feeding programs
- Coordinate with multiple donors
- Serve more people efficiently

### **Shelters & Food Banks**
- Access diverse food sources
- Real-time notifications for new donations
- Better planning and operations

---

## 🔧 Configuration

### **Database Setup**

The application uses Supabase (cloud PostgreSQL). No local database setup required!

Current configuration in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

### **Environment Variables** (Optional)

For production deployment:
```bash
DATABASE_URL=jdbc:postgresql://your-db-url
DB_USERNAME=your-username
DB_PASSWORD=your-password
PORT=8083
```

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### **Areas for Contribution**
- SMS/Email notifications
- Mobile app development
- Route optimization for food pickup
- AI-based food quality assessment
- Multi-language support
- Payment gateway for donations

---

## 📈 Future Enhancements

- [ ] **Mobile App**: Native Android/iOS applications
- [ ] **Real-time Notifications**: Push notifications for new donations
- [ ] **Geolocation**: Map view of nearby donations
- [ ] **Volunteer Network**: Connect volunteers for food pickup/delivery
- [ ] **Rating System**: Review and rate donors/receivers
- [ ] **Gamification**: Badges and rewards for active contributors
- [ ] **Social Media Integration**: Share impact on social platforms
- [ ] **AI Matching**: Smart matching algorithm for optimal distribution
- [ ] **Multi-language**: Support for regional languages
- [ ] **API**: RESTful API for third-party integrations

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Developer Contact

**Kabilesh C**

- 🌐 Portfolio: [Coming Soon]
- 💼 LinkedIn: [linkedin.com/in/kabilesh-c20](https://www.linkedin.com/in/kabilesh-c20)
- 📧 Email: [kabileshc.dev@gmail.com](mailto:kabileshc.dev@gmail.com)
- 🐙 GitHub: [@kabileshc](https://github.com/yourusername)

---

## 🙏 Acknowledgments

- Spring Boot team for the amazing framework
- Supabase for reliable cloud database hosting
- Bootstrap team for the responsive CSS framework
- Chart.js for beautiful data visualizations
- All open-source contributors

---

## 💡 Inspiration

> "There are people in the world so hungry, that God cannot appear to them except in the form of bread." 
> — *Mahatma Gandhi*

This project is inspired by the vision of a world where no one goes hungry while food goes to waste. Every meal shared is a step toward a more compassionate and sustainable society.

---

## 🌟 Support the Project

If you find this project helpful:

- ⭐ Star this repository
- 🍴 Fork and contribute
- 📢 Share with your network
- 🐛 Report issues and bugs
- 💡 Suggest new features

**Together, we can make a difference!**

---

<div align="center">

**Built with ❤️ by Kabilesh C**

*Fighting hunger, one meal at a time*

[![Made with Java](https://img.shields.io/badge/Made%20with-Java-red?style=flat-square&logo=java)](https://www.java.com)
[![Powered by Spring Boot](https://img.shields.io/badge/Powered%20by-Spring%20Boot-brightgreen?style=flat-square&logo=spring)](https://spring.io)

</div>
