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

  <!-- The main loading page of the application -->
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
      <h1>Your Mood. Your Music. Your New Mix.</h1>
      <p>No sign up needed, get started now.</p>
      <form action="form" method="GET" class="width">
        <button type="submit">Get Started</button>
      </form>

    </section>
    </main>
  </body>
</html>

