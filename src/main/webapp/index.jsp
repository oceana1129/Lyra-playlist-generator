<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Lyra</title>
  </head>

  <!-- The main loading page of the application -->
  <body>
    <header>
      <img src="" class="logo" alt="Lyra"/>
      <span class="source">from Spotify Songs</span>
    </header>

    <main>
      <h1>Your Mood. Your Music. Your New Mix.</h1>
      <p>No sign up needed, get started now.</p>
      <form action="form" method="GET">
        <button type="submit">Get Started</button>
      </form>
    </main>
  </body>
</html>

