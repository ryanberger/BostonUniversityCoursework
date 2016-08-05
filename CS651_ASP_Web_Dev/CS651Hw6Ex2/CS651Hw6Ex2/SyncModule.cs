using System;
using System.Collections.Generic;
using System.Data.Sql;
using System.Text;
using System.Web;
using System.Security.Principal;


namespace CS651Hw6Ex2
{
    public class SyncModule : IHttpModule
    {
        public void Init(HttpApplication context)
        {
            // Register our event handler with Application object.
            context.BeginRequest += new EventHandler(OnBeginRequest);
            context.AuthenticateRequest += new EventHandler(this.OnAuthenticateRequest);
        }

        public void OnBeginRequest(Object s, EventArgs e)
        {
            HttpApplication app = s as HttpApplication;
            app.Context.Response.Write("Hello from OnBeginRequest in custom secure module.<br>");
        }

        private void OnAuthenticateRequest(object r_objSender,
                                       EventArgs r_objEventArgs)
        {
            // Authenticate user credentials, and find out user roles
            HttpApplication objApp = (HttpApplication)r_objSender;
            HttpContext objContext = (HttpContext)objApp.Context;
            if ((objApp.Request["userid"] == null) ||
                                (objApp.Request["password"] == null))
            {
                objContext.Response.Write("<H1>Credentials not provided!</H1>");
                objContext.Response.End();
            }

            string userid = "";
            userid = objApp.Request["userid"].ToString();
            string password = "";
            password = objApp.Request["password"].ToString();

            string[] strRoles;
            strRoles = AuthenticateAndGetRoles(userid, password);
            if ((strRoles == null) || (strRoles.GetLength(0) == 0))
            {
                objContext.Response.Write("<H1>We are sorry but we could not find this user id and password in our database</H1>");
                objApp.CompleteRequest();
            }
            else
            {
                objContext.Response.Write("<H1>yup, we found you my dude!</H1>");
                objContext.Response.Write("<br/>The information you requested is: <H2>Code Blue</H2>.");
                GenericIdentity objIdentity = new GenericIdentity(userid, "CustomAuthentication");
                objContext.User = new GenericPrincipal(objIdentity, strRoles);
                objApp.CompleteRequest();
            }
        }

        private string[] AuthenticateAndGetRoles(string r_strUserID, string r_strPassword)
        {
            string[] strRoles = null;
            if ((r_strUserID.Equals("dino")) && (r_strPassword.Equals("rocks")))
            {
                strRoles = new string[1];
                strRoles[0] = "Administrator";
            }
            else if ((r_strUserID.Equals("dino")) && (r_strPassword.Equals("chills")))
            {
                strRoles = new string[1];
                strRoles[0] = "User";
            }
            return strRoles;
        }

        public void Dispose()
        {}
    }

}
