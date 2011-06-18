using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.IO;

using CherryTomato;
using CherryTomato.Entities;
using System.Text;
using System.Windows.Media.Imaging;

namespace RottenTomatoesViewer
{
    public partial class MainPage : UserControl
    {

        public MainPage()
        {
            InitializeComponent();
            DisplayMoviesInTheaters();
        }

        private void DisplayMoviesInTheaters()
        {            
            var url = new Uri(string.Format(@"http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey={0}", Constants.API_KEY));

            GetMovieResults(url);
        }

        private void SearchMovies(string query)
        {
            int pageLimit = 50;
            int startingPage = 1;
            var url = new Uri(string.Format(@"http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey={0}&q={1}&page_limit={2}&page={3}", Constants.API_KEY, query, pageLimit, startingPage));

            GetMovieResults(url);
        }

        private void GetMovieResults(Uri url)
        {
            ClearRightPane();
            myProgessBar.IsIndeterminate = true;
            
            MovieSearchResults movieSearchResults;
            var downloader = new WebClient();
            downloader.OpenReadCompleted += ((sender, e) =>
            {
                Stream inStream = e.Result;

                byte[] buffer = new byte[inStream.Length];
                inStream.Read(buffer, 0, buffer.Length);
                inStream.Close();

                // Convert stream to JSON string and then parse into object
                movieSearchResults = Parser.ParseMovieSearchResults(Encoding.UTF8.GetString(buffer, 0, buffer.Length));

                myProgessBar.IsIndeterminate = false;

                // Populate result list
                MovieList.ItemsSource = movieSearchResults.Results;
            });
            downloader.OpenReadAsync(url);
        }

        private void GetReviews(int movieId)
        {
            var url = new Uri(string.Format(@"http://api.rottentomatoes.com/api/public/v1.0/movies/{0}/reviews.json?apikey={1}", movieId, Constants.API_KEY));

            myProgessBar.IsIndeterminate = true;

			MovieReviews movieReviews;
            var downloader = new WebClient();
            downloader.OpenReadCompleted += ((sender, e) =>
            {
                Stream inStream = e.Result;

                byte[] buffer = new byte[inStream.Length];
                inStream.Read(buffer, 0, buffer.Length);
                inStream.Close();

                // Convert stream to JSON string and then parse into object
                movieReviews = Parser.ParseMovieReviews(Encoding.UTF8.GetString(buffer, 0, buffer.Length));

                myProgessBar.IsIndeterminate = false;

                // Populate result list
                MovieReviewList.ItemsSource = movieReviews.Reviews;
            });
            downloader.OpenReadAsync(url);
        }

        private void GetTheaterResults(string zip, string query)
        {
            var myUrl = string.Format("http://localhost:{0}/GetData.aspx?zip={1}&query={2}", Application.Current.Host.Source.Port, zip, query);

            myProgessBar.IsIndeterminate = true;

            var myService = new WebClient();
            myService.DownloadStringCompleted += ((sender, e) =>
            {
                myProgessBar.IsIndeterminate = false;
                TheaterList.Text = e.Result;
            });
            myService.DownloadStringAsync(new Uri(myUrl));
        }


        private void MoviesList_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            Result movie = (Result)MovieList.SelectedItem;

            ClearRightPane();

            // Fill in movie information
            if (movie != null)
            {
				//Make Theater elements visible
				MovieReviewList.Visibility = Visibility.Visible;
				FindTheaters.Visibility = Visibility.Visible;
				EnterZip.Visibility = Visibility.Visible;
				ZipCode.Visibility = Visibility.Visible;

                MovieTitle.Content = movie.Title;

                var criticRating = movie.Ratings[0].Score;
                if (criticRating > -1)
                {
                    CriticRating.Content = string.Format("Critic rating: {0}%", criticRating);

                    if (criticRating > 60)
                    {
                        FreshOrRotten.Source = new BitmapImage(new Uri("../Images/fresh.jpg", UriKind.Relative));
                    }
                    else
                    {
                        FreshOrRotten.Source = new BitmapImage(new Uri("../Images/rotten.jpg", UriKind.Relative));
                    }
                }

                var userRating = movie.Ratings[1].Score;
                if (userRating > 0)
                {
                    UserRating.Content = string.Format("User rating: {0}%", userRating);
                }

                Runtime.Content = movie.Runtime + " minutes";
                
                MovieImage.Source = new BitmapImage(new Uri(movie.Posters[2].Url));

                if (movie.Cast.Count > 0)
                {
                    string cast = "Cast: ";
                    foreach (CastMember cm in movie.Cast)
                    {
                        cast += cm.Actor + ", ";
                    }
                    Cast.Text = cast.Trim().TrimEnd(',');
                }

                if (!string.IsNullOrEmpty(movie.Synopsis))
                {
                    Summary.Text = string.Format("Summary: {0}", movie.Synopsis);
                }

                GetReviews(movie.RottenTomatoesId);
            }

        }

        private void ClearRightPane()
        {
            // Clear all values
            MovieTitle.Content = string.Empty;
            CriticRating.Content = string.Empty;
            UserRating.Content = string.Empty;
            FreshOrRotten.Source = null;
            MovieImage.Source = null;
            Summary.Text = string.Empty;
            Cast.Text = string.Empty;
            TheaterList.Text = string.Empty;
            Runtime.Content = string.Empty;

            //Make Theater elements invisible
            MovieReviewList.Visibility = Visibility.Collapsed;
            FindTheaters.Visibility = Visibility.Collapsed;
            EnterZip.Visibility = Visibility.Collapsed;
            ZipCode.Visibility = Visibility.Collapsed;
        }

        private void InTheatersButton_Click(object sender, RoutedEventArgs e)
        {
            DisplayMoviesInTheaters();
        }

        private void SearchButton_Click(object sender, RoutedEventArgs e)
        {
            string query = SeachTextBox.Text;

            SearchMovies(query);
        }

        private void FindTheaters_Click(object sender, RoutedEventArgs e)
        {
            string zipCode = ZipCode.Text;
            string query = (string)MovieTitle.Content;

            GetTheaterResults(zipCode, query);
        }

    }
}
