package com.kenaxisq.nestnavigate.utils.property;

public enum Amenity {
    // Land Property Amenities
    ELECTRICITY_CONNECTION(Category.LAND, "Electricity Connection"),
    WATER_SUPPLY(Category.LAND, "Water Supply"),
    SEWAGE_CONNECTION(Category.LAND, "Sewage Connection"),
    FENCING(Category.LAND, "Fencing"),
    ROAD_ACCESS(Category.LAND, "Road Access"),
    GATED_ACCESS(Category.LAND, "Gated Access"),
    IRRIGATION(Category.LAND, "Irrigation"),
    ZONING_APPROVAL(Category.LAND, "Zoning Approval"),
    DRAINAGE_SYSTEM(Category.LAND, "Drainage System"),
    SITE_OFFICE(Category.LAND, "Site Office"),
    LANDSCAPING(Category.LAND, "Landscaping"),
    BOREWELL(Category.LAND, "Borewell"),
    STREET_LIGHTING(Category.LAND, "Street Lighting"),
    PERIMETER_WALL(Category.LAND, "Perimeter Wall"),
    EASEMENT(Category.LAND, "Easement"),

    // Residential Property Amenities
    GYM(Category.RESIDENTIAL, "Gym"),
    SWIMMING_POOL(Category.RESIDENTIAL, "Swimming Pool"),
    PARKING(Category.RESIDENTIAL, "Parking"),
    ELEVATOR(Category.RESIDENTIAL, "Elevator"),
    SECURITY(Category.RESIDENTIAL, "Security"),
    POWER_BACKUP(Category.RESIDENTIAL, "Power Backup"),
    WIFI(Category.RESIDENTIAL, "Wi-Fi"),
    AIR_CONDITIONING(Category.RESIDENTIAL, "Air Conditioning"),
    HEATING(Category.RESIDENTIAL, "Heating"),
    BALCONY(Category.RESIDENTIAL, "Balcony"),
    GARDEN(Category.RESIDENTIAL, "Garden"),
    PLAYGROUND(Category.RESIDENTIAL, "Playground"),
    COMMUNITY_HALL(Category.RESIDENTIAL, "Community Hall"),
    CLUBHOUSE(Category.RESIDENTIAL, "Clubhouse"),
    JOGGING_TRACK(Category.RESIDENTIAL, "Jogging Track"),
    SPORTS_FACILITIES(Category.RESIDENTIAL, "Sports Facilities"),
    HOME_AUTOMATION(Category.RESIDENTIAL, "Home Automation"),
    INTERCOM(Category.RESIDENTIAL, "Intercom"),
    MAINTENANCE_STAFF(Category.RESIDENTIAL, "Maintenance Staff"),
    RAINWATER_HARVESTING(Category.RESIDENTIAL, "Rainwater Harvesting"),
    FIRE_SAFETY(Category.RESIDENTIAL, "Fire Safety"),
    WASTE_DISPOSAL(Category.RESIDENTIAL, "Waste Disposal"),
    LIBRARY(Category.RESIDENTIAL, "Library"),
    RESERVE_PARKING(Category.RESIDENTIAL, "Reserve Parking"),
    VISITOR_PARKING(Category.RESIDENTIAL, "Visitor Parking"),
    PET_FRIENDLY(Category.RESIDENTIAL, "Pet Friendly"),
    SERVANT_ROOM(Category.RESIDENTIAL, "Servant Room"),
    INTERNET(Category.RESIDENTIAL, "Internet"),
    TV_CABLE(Category.RESIDENTIAL, "TV Cable"),
    WATER_PURIFIER(Category.RESIDENTIAL, "Water Purifier"),
    LIFT(Category.RESIDENTIAL, "Lift"),
    PENT_HOUSE(Category.RESIDENTIAL, "PENT_HOUSE"),

    // Commercial Property Amenities
    CONFERENCE_ROOM(Category.COMMERCIAL, "Conference Room"),
    CAFE(Category.COMMERCIAL, "Café"),
    PANTRY(Category.COMMERCIAL, "Pantry"),
    RECEPTION(Category.COMMERCIAL, "Reception"),
    LOADING_DOCKS(Category.COMMERCIAL, "Loading Docks"),
    HIGH_SPEED_INTERNET(Category.COMMERCIAL, "High-Speed Internet"),
    CCTV(Category.COMMERCIAL, "CCTV"),
    FIRE_ALARM(Category.COMMERCIAL, "Fire Alarm"),
    EMERGENCY_EXIT(Category.COMMERCIAL, "Emergency Exit"),
    CONTINUOUS_WATER_SUPPLY(Category.COMMERCIAL, "Continuous Water Supply"),
    CENTRAL_HEATING(Category.COMMERCIAL, "Central Heating"),
    CENTRAL_COOLING(Category.COMMERCIAL, "Central Cooling"),
    MAIL_ROOM(Category.COMMERCIAL, "Mail Room"),
    SERVER_ROOM(Category.COMMERCIAL, "Server Room"),
    BUSINESS_LOUNGE(Category.COMMERCIAL, "Business Lounge"),
    MULTI_LEVEL_PARKING(Category.COMMERCIAL, "Multi-Level Parking"),
    NEAR_PUBLIC_TRANSPORT(Category.COMMERCIAL, "Near Public Transport"),
    SHUTTLE_SERVICE(Category.COMMERCIAL, "Shuttle Service");

    private final Category category;
    private final String description;

    Amenity(Category category, String description) {
        this.category = category;
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

    // Enum for Category
    public enum Category {
        LAND,
        RESIDENTIAL,
        COMMERCIAL;
    }
}