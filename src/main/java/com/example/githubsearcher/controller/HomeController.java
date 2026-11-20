
package com.example.githubsearcher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping(value = "/", produces = "text/html")
    @ResponseBody
    public String home() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>GitHub Repository Searcher</title>
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background: #f4f6f9;
                            margin: 0;
                            padding: 40px 20px;
                            text-align: center;
                        }
                        .container {
                            max-width: 700px;
                            margin: 60px auto;
                            padding: 40px;
                            background: white;
                            border-radius: 14px;
                            box-shadow: 0 4px 18px rgba(0,0,0,0.08);
                        }
                        h1 { color: #24292f; }
                        p { color: #57606a; line-height: 1.7; }
                        a {
                            display: inline-block;
                            margin: 10px;
                            padding: 13px 20px;
                            background: #24292f;
                            color: white;
                            text-decoration: none;
                            border-radius: 7px;
                        }
                        a:hover { background: #0969da; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>GitHub Repository Searcher</h1>
                        <p>Welcome to the GitHub Repository Searcher API.</p>
                        <p>
                            Search GitHub repositories, save repository
                            details in PostgreSQL, and retrieve saved
                            repositories using filters.
                        </p>
                        <a href="/swagger-ui/index.html">
                            Explore API Documentation
                        </a>
                        <a href="/api/github/repositories">
                            View Saved Repositories
                        </a>
                    </div>
                </body>
                </html>
                """;
    }
}
