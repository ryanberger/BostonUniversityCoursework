using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Linq;

using CherryTomato.Entities;
using Newtonsoft.Json.Linq;
using System.Json;

namespace CherryTomato
{
    public static class Parser
    {
        
        public static MovieSearchResults ParseMovieSearchResults(string json)
        {
            JObject jObject = JObject.Parse(json);
            MovieSearchResults results = new MovieSearchResults();

            int resultCount = 0;
            int.TryParse(jObject["total"].ToString(), out resultCount);
            results.ResultCount = resultCount;

            var movies = (JArray) jObject["movies"];
            foreach (var movie in movies)
            {
                Result result = new Result();

                result.Title = (string)movie["title"];

                var tmpIdString = (string)movie["id"];
                string idInStringForm = movie["id"].ToString().Substring(1, tmpIdString.Length);
                result.RottenTomatoesId = Convert.ToInt32(idInStringForm);

                int year = 0;
                int.TryParse(movie["year"].ToString(), out year);
                result.Year = year;
                
                int runtime;
                int.TryParse(movie["runtime"].ToString(), out runtime);
                result.Runtime = runtime;

                result.Synopsis = (string) movie["synopsis"];
                
                var dates = (JObject)movie["release_dates"];
                foreach (var date in dates)
                {
                    ReleaseDate releaseDate = new ReleaseDate();
                    releaseDate.Type = (string)date.Key;
                    var tmpDate = ((string) date.Value).Substring(0, ((string) date.Value).Count());
                    releaseDate.Date = DateTime.Parse(tmpDate);
                    result.ReleaseDates.Add(releaseDate);
                }

                var ratings = (JObject)movie["ratings"];
                foreach (var rating in ratings)
                {
                    Rating newRating = new Rating();
                    newRating.Type = (string)rating.Key;

                    int score = 0;
                    int.TryParse(rating.Value.ToString(), out score);
                    newRating.Score = score;
                    result.Ratings.Add(newRating);
                }

                var posters = (JObject)movie["posters"];
                foreach (var poster in posters)
                {
                    Poster newPoster = new Poster();
                    newPoster.Type = (string)poster.Key;
                    newPoster.Url = (string)poster.Value;
                    result.Posters.Add(newPoster);
                }

                var castMembers = (JArray)movie["abridged_cast"];
                foreach (var castMember in castMembers)
                {
                    CastMember member = new CastMember();
                    member.Actor = (string)castMember["name"];
                    var characters = (JArray)castMember["characters"];
                    if (characters != null)
                    {
                        foreach (var character in characters)
                        {
                            member.Characters.Add((string)character);
                            result.Cast.Add(member);
                        }   
                    }
                }

                var links = (JObject)movie["links"];
                foreach (var link in links)
                {
                    Link newLink = new Link();
                    newLink.Type = (string)link.Key;
                    newLink.Url = (string)link.Value;
                    result.Links.Add(newLink);
                }

                results.Results.Add(result);
            }

            return results;
        }

        public static MovieReviews ParseMovieReviews(string json)
        {
            JObject jObject = JObject.Parse(json);
            MovieReviews results = new MovieReviews();

            int resultCount = 0;
            int.TryParse(jObject["total"].ToString(), out resultCount);
            results.ResultCount = resultCount;

            var reviews = (JArray) jObject["reviews"];
            foreach (var review in reviews)
            {
                Review result = new Review();

                result.Critic = (string)review["critic"];
                result.Date = (string)review["date"];
                result.Publication = (string)review["publication"];
                result.Quote = (string)review["quote"];

                var links = (JObject)review["links"];
                foreach (var link in links)
                {
                    Link newLink = new Link();
                    newLink.Type = (string)link.Key;
                    newLink.Url = (string)link.Value;
                    result.Links.Add(newLink);
                }
                results.Reviews.Add(result);
            }

            return results;
        }
    }
}
