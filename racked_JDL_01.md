
application {
  config {
    baseName RACKED
    applicationType monolith
    packageName zipcode.rocks
    authenticationType jwt
    prodDatabaseType mysql
    clientFramework react
  }
  entities *
}

entity Rack {
	location Location
    reviewAggregate ReviewAggregate
    sizeAggregate SizeAggregate
    image Image
    starAggregate StarAggregate
    
}

entity Review {
	location Location
    image Image
    size Size
    locationStar StarRating
    securityStar StarRating
    generalStar StarRating
    ammenity Ammenity
}

entity Ammenity {
	name String
}

entity Location {
	distanceFromUser Integer
    gpsCoordinates String
}

entity Image{
 	imageLocation String
 	image ImageBlob
}

enum Size {
  	SMALL("Small"),
  	MEDIUM("Medium"),
  	LARGE("Large"),
  	XL("Extra-Large")
}


enum StarRating{
	ONE("One"), 
    TWO("Two"),
    THREE("Three"),
    FOUR("Four"),
    FIVE("Five")
}

relationship OneToMany {
  	Rack to Image
  	Rack to Review
  	Location to Rack
  	User to Review
  	Review to Ammenity
  	Review to StarRating
}

relationship ManyToMany {
	User to Rack
}
