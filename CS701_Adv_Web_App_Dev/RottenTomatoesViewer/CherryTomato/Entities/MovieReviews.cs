using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CherryTomato.Entities
{
    public class MovieReviews
    {
        public int ResultCount { get; set; }
        public List<Review> Reviews { get; set; }

        public MovieReviews()
        {
            Reviews = new List<Review>();
        }
    }

    public class Review
    {
        public string Critic { get; set; }
        public string Date { get; set; }
        public string Publication { get; set; }
        public string Quote { get; set; }
        public List<Link> Links { get; set; }

        public Review()
        {
            Links = new List<Link>();
        }
    }
}
