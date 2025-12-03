<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%-- The primary form of the application --%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <!-- Meta Information -->
        <title>Lyra Playlist Generator</title>

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

        <!-- Font -->
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
          href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,200..800;1,200..800&family=Roboto:ital,wght@0,100..900;1,100..900&display=swap"
          rel="stylesheet"
        />

        <!-- Style Sheets -->
        <link rel="stylesheet" href="css/reset.css" />
        <link rel="stylesheet" href="css/style.css" />
  </head>

  <body>
     <header>
       <a href="index.jsp">
		    <img
		        src="https://res.cloudinary.com/oceana-web-designs/image/upload/v1764196146/lyra-logo_zthqse.png"
		        alt="Lyra logo"
	      />
	    </a>
       <a href="https://open.spotify.com/" target="_blank">
      	<span class="source">from Spotify Songs</span>
      </a>
     </header>

    <form action="results" method="POST">
      <!-- EMOTION SECTION -->
      <section>
      <h2>Pick an emotion(s)</h2>
      <p>What is the tone of your playlist?</p>
      <div class="input-container">
        <input type="checkbox" id="happy" name="emotion" value="happy" />
        <label for="happy">Happy</label>
        <input type="checkbox" id="sad" name="emotion" value="sad" />
        <label for="sad">Sad</label>
        <input type="checkbox" id="angry" name="emotion" value="angry" />
        <label for="angry">Angry</label>
        <input type="checkbox" id="scared" name="emotion" value="scared" />
        <label for="scared">Scared</label>
        <input
          type="checkbox"
          id="surprised"
          name="emotion"
          value="surprised"
        />
        <label for="surprised">Surprised</label>
      </div>
    </section>

      <!-- GENRE SECTION -->
      <section>
	      <h2>Pick a genre(s)</h2>
	      <p>What is the style of music in your playlist?</p>
	      <div class="input-container">
	        <input type="checkbox" id="pop" name="genre" value="pop" />
	        <label for="pop">Pop</label>
	        <input type="checkbox" id="indiePop" name="genre" value="indiePop" />
	        <label for="indiePop">Indie Pop</label>
	        <input type="checkbox" id="synthPop" name="genre" value="synthPop" />
	        <label for="synthPop">Synth Pop</label>
	        <input type="checkbox" id="country" name="genre" value="country" />
	        <label for="country">Country</label>
	        <input type="checkbox" id="folk" name="genre" value="folk" />
	        <label for="folk">Folk</label>
	        <input type="checkbox" id="hipHop" name="genre" value="hipHop" />
	        <label for="hipHop">Hip Hop</label>
	        <input type="checkbox" id="rap" name="genre" value="rap" />
	        <label for="rap">Rap</label>
	        <input type="checkbox" id="trap" name="genre" value="trap" />
	        <label for="trap">Trap</label>
	        <input type="checkbox" id="jazz" name="genre" value="jazz" />
	        <label for="jazz">Jazz</label>
	        <input type="checkbox" id="rock" name="genre" value="rock" />
	        <label for="rock">Rock</label>
	        <input type="checkbox" id="hardRock" name="genre" value="hardRock" />
	        <label for="hardRock">Hard Rock</label>
	        <input type="checkbox" id="postPunk" name="genre" value="postPunk" />
	        <label for="postPunk">Post Punk</label>
	        <input type="checkbox" id="punk" name="genre" value="punk" />
	        <label for="punk">Punk</label>
	        <input type="checkbox" id="deathMetal" name="genre" value="deathMetal" />
	        <label for="deathMetal">Death Metal</label>
	        <input type="checkbox" id="classical" name="genre" value="classical" />
	        <label for="classical">Classical</label>
	        <input type="checkbox" id="electronic" name="genre" value="electronic" />
	        <label for="electronic">Electronic</label>
	        <input type="checkbox" id="alternative" name="genre" value="alternative" />
	        <label for="alternative">Alternative</label>
	      </div>
	    </section>

      <!-- ACTIVITY SECTION -->
      <section>
        <h2>Pick an activity</h2>
        <p>What activity would your playlist be best suited for?</p>
        <div class="input-container">
          <input type="checkbox" id="party" name="activity" value="party" />
          <label for="party">Party</label>
          <input type="checkbox" id="studying" name="activity" value="studying" />
          <label for="studying">Studying</label>
          <input type="checkbox" id="relaxation" name="activity" value="relaxation" />
          <label for="relaxation">Relaxation</label>
          <input type="checkbox" id="exercise" name="activity" value="exercise" />
          <label for="exercise">Exercise</label>
          <input type="checkbox" id="running" name="activity" value="running" />
          <label for="running">Running</label>
          <input type="checkbox" id="yoga" name="activity" value="yoga" />
          <label for="yoga">Yoga</label>
          <input type="checkbox" id="driving" name="activity" value="driving" />
          <label for="driving">Driving</label>
          <input type="checkbox" id="social" name="activity" value="social" />
          <label for="social">Social</label>
          <input type="checkbox" id="morning" name="activity" value="morning" />
          <label for="morning">Morning</label>
        </div>
      </section>

      <!-- AUDIO FEATURES SECTION -->
      <section>
        <h2>Select Audio Features</h2>
        <p>Tune the feeling of your playlist</p>
        <div class="input-container">
          <div class="range-input">
            <label>Popularity</label>
            <p>How popular should your music be?</p>
            <input type="range" name="popularity"  min="0" max="100" value="50"/>
            <div class="range-labels">
              <span>0</span>
              <span>100</span>
            </div>
          </div>

          <div class="range-input">
            <label>Energy</label>
            <p>What is the energy levels of your music?</p>
            <input type="range" name="energy"  min="0" max="100" value="50"/>
            <div class="range-labels">
              <span>Calm</span>
              <span>High Energy</span>
            </div>
          </div>

          <div class="range-input">
            <label>Danceability</label>
            <p>How easy should it be to dance to your music?</p>
            <input type="range" name="danceability"  min="0" max="100" value="50"/>
            <div class="range-labels">
              <span>Not Danceable</span>
              <span>Very Danceable</span>
            </div>
          </div>

          <div class="range-input">
            <label>Positivity</label>
            <p>How uplifting should your music be?</p>
            <input type="range" name="positivity"  min="0" max="100" value="50"/>
            <div class="range-labels">
              <span>Dark</span>
              <span>Cheerful</span>
            </div>
          </div>

          <div class="range-input">
            <label>Instrumentalness</label>
            <p>How much of your music should be instrumental versus vocals?</p>
            <input type="range" name="instrumentalness" class="slider"  min="0" max="100" value="50"/>
            <div class="range-labels">
              <span>Vocals</span>
              <span>Mainly Instruments</span>
            </div>
          </div>
        </div>
      </section>
		
      <button type="submit">Generate Playlist</button>
      <div style="height: 10vh"></div>
    </form>
  </body>
</html>

