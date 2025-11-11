<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%-- The primary form of the application --%>
<!DOCTYPE html>
<html>
  <head>
    <title>Create Your Playlist</title>
  </head>
  <body>
     <header>
        <img src="" class="logo" alt="lyra">
        <span class="source">from Spotify Songs</span>
    </header>

    <form action="results" method="POST">
      <!-- EMOTION SECTION -->
      <section>
        <h2>Pick an emotion</h2>
        <p>What is the tone of your playlist?</p>
        <div>
          <input type="checkbox" name="emotion" value="happy" />
          <label for="happy">Happy</label>
          <input type="checkbox" name="emotion" value="sad" />
          <label for="sad">Sad</label>
          <input type="checkbox" name="emotion" value="angry" />
          <label for="angry">Angry</label>
          <input type="checkbox" name="emotion" value="scared" />
          <label for="scared">Scared</label>
          <input type="checkbox" name="emotion" value="surprised" />
          <label for="surprised">Surprised</label>
        </div>
      </section>

      <!-- GENRE SECTION -->
      <section>
        <h2>Pick a genre(s)</h2>
        <p>What is the style of music in your playlist?</p>
        <div>
          <input type="checkbox" name="genre" value="pop" />
          <label for="pop">Pop</label>
          <input type="checkbox" name="genre" value="indiePop" />
          <label for="indiePop">Indie Pop</label>
          <input type="checkbox" name="genre" value="synthPop" />
          <label for="synthPop">Synthpop</label>
          <input type="checkbox" name="genre" value="country" />
          <label for="country">Country</label>
          <input type="checkbox" name="genre" value="folk" />
          <label for="folk">Folk</label>
          <input type="checkbox" name="genre" value="hipHop" />
          <label for="hipHop">Hip Hop</label>
          <input type="checkbox" name="genre" value="rap" />
          <label for="rap">Rap</label>
          <input type="checkbox" name="genre" value="trap" />
          <label for="trap">Trap</label>
          <input type="checkbox" name="genre" value="jazz" />
          <label for="jazz">Jazz</label>
          <input type="checkbox" name="genre" value="rock" />
          <label for="rock">Rock</label>
          <input type="checkbox" name="genre" value="hardRock" />
          <label for="hardRock">Hard Rock</label>
          <input type="checkbox" name="genre" value="postPunk" />
          <label for="postPunk">Post Punk</label>
          <input type="checkbox" name="genre" value="punk" />
          <label for="punk">Punk</label>
          <input type="checkbox" name="genre" value="deathMetal" />
          <label for="deathMetal">Death Metal</label>
          <input type="checkbox" name="genre" value="classical" />
          <label for="classical">Classical</label>
          <input type="checkbox" name="genre" value="electronic" />
          <label for="electronic">Electronic</label>
          <input type="checkbox" name="genre" value="alternative" />
          <label for="alternative">Alternative</label>
        </div>
      </section>

      <!-- ACTIVITY SECTION -->
      <section>
        <h2>Pick an activity</h2>
        <p>What activity would your playlist be best suited for?</p>
        <div>
          <input type="checkbox" name="activity" value="party" />
          <label for="party">Party</label>
          <input type="checkbox" name="activity" value="studying" />
          <label for="studying">Studying</label>
          <input type="checkbox" name="activity" value="relaxation" />
          <label for="relaxation">Relaxation</label>
          <input type="checkbox" name="activity" value="exercise" />
          <label for="exercise">Exercise</label>
          <input type="checkbox" name="activity" value="running" />
          <label for="running">Running</label>
          <input type="checkbox" name="activity" value="yoga" />
          <label for="yoga">Yoga</label>
          <input type="checkbox" name="activity" value="driving" />
          <label for="driving">Driving</label>
          <input type="checkbox" name="activity" value="social" />
          <label for="social">Social</label>
          <input type="checkbox" name="activity" value="morning" />
          <label for="morning">Morning</label>
        </div>
      </section>

      <!-- AUDIO FEATURES SECTION -->
      <section>
        <h2>Select Audio Features</h2>
        <p>Tune the feeling of your playlist</p>

        <label>Popularity</label>
        <input type="range" name="popularity" />

        <label>Energy</label>
        <input type="range" name="energy" />

        <label>Danceability</label>
        <input type="range" name="danceability" />

        <label>Positivity</label>
        <input type="range" name="positivity" />

        <label>Instrumentalness</label>
        <input type="range" name="instrumentalness" />
      </section>

      <!-- PLAYLIST NAME -->
      <section>
        <h2>What is your playlist called?</h2>
        <p>Name your personal creation</p>
        <input type="text" name="playlistName" placeholder="Enter name here" />
      </section>

      <button type="submit">Generate Playlist</button>
    </form>
  </body>
</html>

