<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!-- The results page of the application -->
<!DOCTYPE html>
<html>
<head>
    <title>Your Playlist</title>
</head>
<body>

 <header>
     <img src="" class="logo" alt="Lyra">
     <span class="source">from Spotify Songs</span>
 </header>

<main>
    <h1>Your Playlist</h1>
    <p>These are the songs we recommend for you</p>

    <section>
    	<c:choose>
        	<c:when test="${not empty recommendedSongs}">
            <h2>Recommended Songs:</h2>
                <c:forEach var="song" items="${recommendedSongs}" varStatus="i">
				    <div class="song">
				        <h4>${song.title}</h4>
				
				        <h5>
				            <c:forEach var="a" items="${artistsPerSong[i.index]}" varStatus="loop">
				                ${a.name}<c:if test="${!loop.last}">, </c:if>
				            </c:forEach>
				        </h5>
				    </div>
				</c:forEach>
       		</c:when>
        <c:otherwise>
            <p>No matching songs found for your selection.</p>
        </c:otherwise>
    </c:choose>
    </section>
</main>

</body>
</html>