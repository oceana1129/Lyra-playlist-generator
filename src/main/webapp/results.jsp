<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Lyra Your Playlist</title>

    <meta name="theme-color" content="#ea8fdc" />

    <link
      rel="icon"
      href="https://res.cloudinary.com/oceana-web-designs/image/upload/v1764196227/sound_adov9j.png"
      type="image/icon type"
    />

    <link
      rel="apple-touch-icon"
      sizes="75x75"
      href="https://res.cloudinary.com/oceana-web-designs/image/upload/v1764196227/sound_adov9j.png"
    />

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,200..800;1,200..800&family=Roboto:ital,wght@0,100..900;1,100..900&display=swap"
      rel="stylesheet"
    />

    <link rel="stylesheet" href="css/reset.css" />
    <link rel="stylesheet" href="css/style.css" />
</head>

<body>

<header>
    <img
      src="https://res.cloudinary.com/oceana-web-designs/image/upload/v1764196146/lyra-logo_zthqse.png"
      alt="Lyra logo"
    />
    <span class="source">from Spotify Songs</span>
</header>

<main>
    <section>
    
	    <h1>Your Playlist</h1>
	    <p>These are the songs we recommend for you</p>
    	<c:choose>
        	<c:when test="${not empty recommendedSongs}">
        		<div class="song-container">
                	<c:forEach var="song" items="${recommendedSongs}" varStatus="i">
                
				    <div class="song">
				    	<div>
				    		<h4>${song.title}</h4>
					        <h5>
					            <c:forEach var="a" items="${artistsPerSong[i.index]}" varStatus="loop">
					                ${a.name}<c:if test="${!loop.last}">, </c:if>
					            </c:forEach>
					        </h5>
				    	</div>
				        <div>
				            <img
				              src="https://res.cloudinary.com/oceana-web-designs/image/upload/v1764199096/sound_vks86y.png"
				              alt="song icon"
				            />
			          </div>
				    </div>
				</c:forEach>
                </div>
       		</c:when>
        <c:otherwise>
            <p class="centerMe">No matching songs found for your selection.</p>
        </c:otherwise>
    </c:choose>
    </section>
</main>

</body>
</html>
