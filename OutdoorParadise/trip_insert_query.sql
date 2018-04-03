INSERT INTO Trip(description, duration, price, min_participants, max_participants, children_allowed) 
	VALUES	('Frankrijk', 5, 25.0, 2, 8, 0),
			('Engenland', 7, 30.0, 5, 6, 1);

INSERT INTO Excursion(description, guide, price)
	VALUES	('Mountainbike', 0, 10.0),
			('Fietsen', 1, 20.0);

INSERT INTO Trip_Product(product, trip, discount)
	VALUES	(1, 1, 0.1),
			(2, 1, 0.1),
			(3, 2, 0.2),
			(4, 2, 0.2)