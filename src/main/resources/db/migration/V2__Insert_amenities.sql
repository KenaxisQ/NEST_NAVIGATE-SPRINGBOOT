INSERT INTO amenity (id, name, category, sub_category) VALUES
-- Land Amenities
(UUID(), 'Electricity Connection', 'LAND', 'Amenities'),
(UUID(), 'Water Supply', 'LAND', 'Amenities'),
(UUID(), 'Sewage Connection', 'LAND', 'Amenities'),
(UUID(), 'Fencing', 'LAND', 'Amenities'),
(UUID(), 'Road Access', 'LAND', 'Amenities'),
(UUID(), 'Gated Access', 'LAND', 'Amenities'),
(UUID(), 'Irrigation', 'LAND', 'Amenities'),
(UUID(), 'Zoning Approval', 'LAND', 'Amenities'),
(UUID(), 'Drainage System', 'LAND', 'Amenities'),
(UUID(), 'Site Office', 'LAND', 'Amenities'),
(UUID(), 'Landscaping', 'LAND', 'Amenities'),
(UUID(), 'Borewell', 'LAND', 'Amenities'),
(UUID(), 'Street Lighting', 'LAND', 'Amenities'),
(UUID(), 'Perimeter Wall', 'LAND', 'Amenities'),
(UUID(), 'Easement', 'LAND', 'Amenities'),
(UUID(), 'Ready to Move In', 'LAND', 'Miscellaneous'),
(UUID(), 'Financing Provided', 'LAND', 'Miscellaneous'),
(UUID(), 'Ownership documents provided', 'LAND', 'Miscellaneous'),
(UUID(), 'Taxi Stand', 'LAND', 'Near By Services'),
(UUID(), 'Natural Park', 'LAND', 'Near By Services'),
(UUID(), 'Shopping Complex', 'LAND', 'Near By Services'),
(UUID(), 'Hospital', 'LAND', 'Near By Services'),
(UUID(), 'Bus Stand', 'LAND', 'Near By Services'),
(UUID(), 'Railway Station', 'LAND', 'Near By Services'),
-- Residential Amenities
(UUID(), 'Gym', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Swimming Pool', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Parking', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Elevator', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Security', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Power Backup', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Wi-Fi', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Air Conditioning', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Heating', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Balcony', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Garden', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Playground', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Community Hall', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Clubhouse', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Jogging Track', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Sports Facilities', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Home Automation', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Intercom', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Maintenance Staff', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Rainwater Harvesting', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Fire Safety', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Waste Disposal', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Library', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Reserve Parking', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Visitor Parking', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Pet Friendly', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Servant Room', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Internet', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'TV Cable', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Water Purifier', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Lift', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Pent House', 'RESIDENTIAL', 'Amenities'),
(UUID(), 'Ready to Move In', 'RESIDENTIAL', 'Miscellaneous'),
(UUID(), 'Financing Provided', 'RESIDENTIAL', 'Miscellaneous'),
(UUID(), 'Ownership documents provided', 'RESIDENTIAL', 'Miscellaneous'),
(UUID(), 'Taxi Stand', 'RESIDENTIAL', 'Near By Services'),
(UUID(), 'Natural Park', 'RESIDENTIAL', 'Near By Services'),
(UUID(), 'Shopping Complex', 'RESIDENTIAL', 'Near By Services'),
(UUID(), 'Hospital', 'RESIDENTIAL', 'Near By Services'),
(UUID(), 'Bus Stand', 'RESIDENTIAL', 'Near By Services'),
(UUID(), 'Railway Station', 'RESIDENTIAL', 'Near By Services'),

-- Commercial Amenities
(UUID(), 'Conference Room', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Café', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Pantry', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Reception', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Loading Docks', 'COMMERCIAL', 'Amenities'),
(UUID(), 'High-Speed Internet', 'COMMERCIAL', 'Amenities'),
(UUID(), 'CCTV', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Fire Alarm', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Emergency Exit', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Continuous Water Supply', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Central Heating', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Central Cooling', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Mail Room', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Server Room', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Business Lounge', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Multi-Level Parking', 'COMMERCIAL', 'Amenities'),
(UUID(), 'Ready to Move In', 'COMMERCIAL', 'Miscellaneous'),
(UUID(), 'Financing Provided', 'COMMERCIAL', 'Miscellaneous'),
(UUID(), 'Ownership documents provided', 'COMMERCIAL', 'Miscellaneous'),
(UUID(), 'Taxi Stand', 'COMMERCIAL', 'Near By Services'),
(UUID(), 'Natural Park', 'COMMERCIAL', 'Near By Services'),
(UUID(), 'Shopping Complex', 'COMMERCIAL', 'Near By Services'),
(UUID(), 'Hospital', 'COMMERCIAL', 'Near By Services'),
(UUID(), 'Bus Stand', 'COMMERCIAL', 'Near By Services'),
(UUID(), 'Railway Station', 'COMMERCIAL', 'Near By Services'),

-- PG Amenities
(UUID(), 'Furnished Rooms (Bed, Table, Chair, Wardrobe)', 'PG', 'Amenities'),
(UUID(), 'Electricity', 'PG', 'Amenities'),
(UUID(), 'Continuous Water Supply', 'PG', 'Amenities'),
(UUID(), 'Wi-Fi / High-Speed Internet', 'PG', 'Amenities'),
(UUID(), 'Attached Bathrooms', 'PG', 'Amenities'),
(UUID(), 'Laundry Facilities', 'PG', 'Amenities'),
(UUID(), 'Housekeeping Services', 'PG', 'Amenities'),
(UUID(), 'Power Backup', 'PG', 'Amenities'),
(UUID(), 'Mess/Meals (Breakfast, Lunch, Dinner)', 'PG', 'Amenities'),
(UUID(), 'Shared Kitchen (For Self-Cooking)', 'PG', 'Amenities'),
(UUID(), 'Refrigerator', 'PG', 'Amenities'),
(UUID(), 'CCTV Surveillance', 'PG', 'Amenities'),
(UUID(), 'Biometric/Key Card Access', 'PG', 'Amenities'),
(UUID(), '24/7 Security Guard', 'PG', 'Amenities'),
(UUID(), 'Common Lounge/TV Area', 'PG', 'Amenities'),
(UUID(), 'Indoor Games (Chess, Carrom, etc.)', 'PG', 'Amenities'),
(UUID(), 'Gym/Fitness Room', 'PG', 'Amenities'),
(UUID(), 'Air Conditioning/Heating', 'PG', 'Amenities'),
(UUID(), 'Parking Facilities (For Two-Wheelers/Cars)', 'PG', 'Amenities'),
(UUID(), 'Public Transport Accessibility', 'PG', 'Amenities'),
(UUID(), 'Nearby Markets', 'PG', 'Amenities'),
(UUID(), 'Shuttle Service', 'PG', 'Amenities'),
(UUID(), 'Visitor Policy', 'PG', 'Miscellaneous'),
(UUID(), 'Storage Space', 'PG', 'Miscellaneous'),
(UUID(), 'Drinking Water Facility (RO/Filter)', 'PG', 'Miscellaneous'),
(UUID(), 'Taxi Stand', 'PG', 'Near By Services'),
(UUID(), 'Natural Park', 'PG', 'Near By Services'),
(UUID(), 'Shopping Complex', 'PG', 'Near By Services'),
(UUID(), 'Hospital', 'PG', 'Near By Services'),
(UUID(), 'Bus Stand', 'PG', 'Near By Services'),
(UUID(), 'Railway Station', 'PG', 'Near By Services');