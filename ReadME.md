# Flickr Gallery

__This is an Android Application that is used to fetch photos from [Flickr website](https://www.flickr.com) by Flickr API and show it as a Gallery__

## First

	* I used the __Flickr API Documentation__ to know how to get the links of the most popular photos on the [Flickr website](https://www.flickr.com)
	* Then I found Method called: [flickr.interestingness.getList](https://www.flickr.com/services/api/explore/flickr.interestingness.getList) which is the way to get the most interesting photos of the day 
	* I got the API Key from the website to get the access of the photos from the API
	
## Second 

	* Started to develop the App with the instructions to develop it correctly 
	* I used JSON Data to be fetched in the app
	* To fetch the JSON file, I used AsyncTask class and got the data from the API
	* Created the SQLite local database to store the links of images after downloading the JSON file
	
## Third
	
	__In Msin Activity__
	
	* I used the RecyclerView as a GridView by using the StaggeredGridLayoutManager to handle the colomns and spans of each view 
	* Picasso Library was used to download and cache the images in the GridAdapter after getting the links
	![screenshot here](/screens/screen1.png)
	* SwipeRefreshLayout was used to refresh the page by swipe down to get the new images from the server
	![screenshot here](/screens/screen2.png)
	* The Activity is refreshed every minute using Timer class to get any new photos is available
	* The RecyclerView get more items by scrolling down 
	
## Forth
	
	__In Full Screen Activity__
	
	* the View has a custom ViewPager called ExtendedViewPager to use it with a custom Imageview called TouchImageView
	![screenshot here](/screens/screen3.png)
	* the TouchImageView can zoom in and zoom out using pinch zoom and double tap zoom
	* We can so easily navigate the other photos by sliding right or left in the viewpager itself
	![screenshot here](/screens/screen4.png)
	
	![here is the APK](/APK/app_debug.apk)
	
	