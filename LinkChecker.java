/* Output will demonstrate the program working on the following URLs:
 * http://computing.dcu.ie/~humphrys/computers.internet.links.html
 * http://computing.dcu.ie/~humphrys/news.links.html
 * http://humphrysfamilytree.com/links.html
 * http://humphrysfamilytree.com/sources.html
 * http://humphrysfamilytree.com/sources.local.html
 */ 

/*
 * Course: CA651 - Introduction to Networks and Operating Systems
 * Lecturer: Mark Humphrys
 * Student name: Janice Brogan
 * Student number: 14211493
 */
 
 /*
 * Practical Title: Java practical - Java link checker
 * Date created: 27/03/2015
 * Date last modified: 06/04/2015
 */

import java.net.*;
import java.util.*;
import java.io.*;
import javax.swing.text.html.parser.*;
import javax.swing.text.html.*;
import javax.swing.text.*;

/**
 *
 * @author Janice Brogan (Student No. 14211493)
 * @version 1.5
 *
 */

public class LinkChecker {
	/**
     * The below main method is the driver of the programme. The 'Steps' refer to the points made in the 
	 * project specifications.
	 *
	 * @param  args  the String (which will be the URL, in this case) taken as a command-line argument
	 * @throws 		 Exception
	 */
	public static void main (String[] args) throws Exception {
	
		try {
			/* Steps 1-3: Pass the URL that was taken as a command-line argument into the downloadPage method.
			 * This method will catch errors if it's a bad URL or if the URL is not found. If it's a good URL,
			 * download the page and return the URL. The driver stores what is returned by the method in a new 
			 * String called goodURL.*/
			String goodURL = downloadPage(args[0]);
			
			/* Steps 4-5: Pass goodURL into the extractLinks method, which extracts all links in the page by 
			 * parsing its HTML and stores the links in an ArrayList.
			 * The driver stores what is returned by the method in an ArrayList of Strings called allLinksFound.*/
			ArrayList<String> allLinksFound = extractLinks(goodURL);
			
			/* Steps 6-9: Pass allLinksFound into the processLinks method, which 'filters' all the links by 
			 * removing duplicate links and disregarding those that link to Google. The method then finds all 
			 * broken links and passes them to the internal printBrokenLinks method, whereby links are written
			 * to a new web page that can be browsed (offline) by clicking on the links. goodURL is passed to
			 * this method also so that it can be referenced in the printBrokenLinks method.*/
			processLinks(allLinksFound, goodURL);
		} 
		catch (Exception e) {
			System.out.println("Exception was thrown:");
			System.out.println("Exception message: " + e.getMessage());
		}
	}
	
	/**
	 * The following method has been taken (and adapted) from the source below: 
	 * http://computing.dcu.ie/~humphrys/Notes/Networks/java.html
	 * 
	 * This method will catch errors if the URL is bad or not found. If it is a good URL, download the page,
	 * (i.e. write it to a new file and output its code to the command line).
	 *
	 * @param  cmdLineURL  the URL taken as a command-line argument
	 * @return  		   the URL String
	 * @throws IOException
	 */
	public static String downloadPage(String cmdLineURL) throws IOException  {
		try {
			URL urlAddress = new URL(cmdLineURL);
			
			// Create a reader to read the page of the URL.
			BufferedReader pageReader = new BufferedReader(new InputStreamReader(urlAddress.openStream()));
			
			// Create a String which will represent each line on the page as we read through it.
			String pageLine;
			
			/* Create the file path that the downloaded page will be saved as (saving to the current
			 * working directory).*/
			String outputFilePath = System.getProperty("user.dir") + File.separator + "goodUrlOutput.html";
			
			PrintWriter output = new PrintWriter(outputFilePath);

			// While there are still lines on the page to be read:
			while ((pageLine = pageReader.readLine()) != null) {
				System.out.println(pageLine); // Print web page code to console.
				output.println(pageLine); // Print web page code to a new file.
			}
			
			// Close the BufferedReader stream.
			pageReader.close();
			
			// Close the PrintWriter stream.
			output.close();
		} 
		catch (MalformedURLException e) {
			System.out.println("MalformedURLException was thrown: bad URL.");
			System.out.println("Exception message: " + e.getMessage());
		} 
		catch (IOException e) {
			System.out.println("IOException was thrown: URL not found.");
			System.out.println("Exception message: " + e.getMessage());
		}
		
		// Return the URL.
		return cmdLineURL;
	}

	
	/**
	 * The following method - and the comments surrounded by quotation marks - has been taken
	 * (and adapted) from the source below:
	 * http://www.exampledepot.8waytrips.com/egs/javax.swing.text.html/GetLinks.html
	 * 
	 * This method parses the HTML of the URL and extracts all HREF links contained within.
	 *
	 * @param  verifiedURL  the URL that was taken as a command-line argument and verified as a 'good' URL
	 * @return  		    an ArrayList of Strings containing the extracted links
	 * @throws URISyntaxException
	 */
	public static ArrayList<String> extractLinks(String verifiedURL) throws URISyntaxException {
	
		// Create new ArrayList to store the links identified on the web page.
		ArrayList<String> storedLinks = new ArrayList<String>();
	
		try {
			// "Create a reader on the HTML content"
			URL storedUrl = new URI(verifiedURL).toURL();
			URLConnection conn = storedUrl.openConnection();
			Reader readStream = new InputStreamReader(conn.getInputStream());

			// "Parse the HTML"
			EditorKit parserKit = new HTMLEditorKit();
			HTMLDocument doc = (HTMLDocument)parserKit.createDefaultDocument();
			parserKit.read(readStream, doc, 0);

			// "Find all the A elements in the HTML document"
			HTMLDocument.Iterator itrHTML = doc.getIterator(HTML.Tag.A);
			while (itrHTML.isValid()) {
				SimpleAttributeSet attSet = (SimpleAttributeSet)itrHTML.getAttributes();

				String link = (String)attSet.getAttribute(HTML.Attribute.HREF);
				
				// If a link exists, add it to the storedLinks ArrayList.
				if (link != null) {
					storedLinks.add(link);
				}
				itrHTML.next();
			}
			
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException was thrown: bad URL.");
			System.out.println("Exception message: " + e.getMessage());
		} catch (URISyntaxException e) {
			System.out.println("URISyntaxException was thrown.");
			System.out.println("Exception message: " + e.getMessage());
		} catch (BadLocationException e) {
			System.out.println("BadLocationException was thrown.");
			System.out.println("Exception message: " + e.getMessage());
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException was thrown: IP address could not be found for this host.");
			System.out.println("Exception message: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException was thrown: URL not found.");
			System.out.println("Exception message: " + e.getMessage());
		}
		
		// Return all the links extracted.
		return storedLinks;
	}
	
	/**
	 * This method iterates through all the links that were extracted from the URL that was taken as a 
	 * command-line argument. The method first 'filters' all the links by removing duplicate links. It then 
	 * 'filters out' any links to Google and identifies broken links. In this case, a broken link constitutes 
	 * any link with a HTTP response code of 404, or a link that times out. The latter two conditions 
	 * are tested by calling the auxiliary method isURLbroken (in which the timeout is set to 17 seconds).
	 * Finally, the method passes the resulting lists of links to the internal printBrokenLinks method,
	 * whereby links are written to a new web page that can be browsed (offline) by clicking on the links.
	 *
	 * @param  extractedLinks  an ArrayList of Strings containing the extracted links
	 * @param  verifiedURL 	   the URL that was taken as a command-line argument and verified as a good URL
	 * @throws Exception
	 */
	public static void processLinks (ArrayList<String> extractedLinks, String verifiedURL) throws Exception {
		
		// Create new ArrayList to later store the unique links, after the extractedLinks are 'filtered'.
		ArrayList<String> uniqueLinks = new ArrayList<String>();
		
		// Remove any duplicate links from extractedLinks.
		// Begin by creating a temporary HashSet (a data structure that removes duplicates).
		HashSet<String> tempSet = new HashSet<String>();
		// Add everything from extractedLinks into tempSet to remove duplicates.
		tempSet.addAll(extractedLinks);
		// Then, add the resulting unique links into the uniqueLinks ArrayList.
		uniqueLinks.addAll(tempSet);
		
		// Create new ArrayList to store the links identified as having a response code of 404.
		ArrayList<String> badResponseLinks = new ArrayList<String>();
		
		// Create new ArrayList to store the links identified as having a timeout in excess of 17 seconds.
		ArrayList<String> timeOutLinks = new ArrayList<String>();
		
		// Create new String that will represent each link in the uniqueLinks ArrayList.
		String singleLink;
		
		try {
			// For every String in the uniqueLinks ArrayList:
			for (Iterator<String> itr = uniqueLinks.listIterator(); itr.hasNext();) {
				singleLink = itr.next();
				
				/* Pass the link into the isURLbroken method. Store what is returned by the method in the integer 
				 * called linkReturn.*/
				int linkReturn = isURLbroken(singleLink);
				
				// If the link is not to Google:
				if ((!singleLink.contains(".google"))) {
					/* Then, if the returned value from isURLbroken is 1 or 2 (indicating that the link times out 
					 * after 17 seconds), store it in timeOutLinks.*/
					if (linkReturn == 1 || linkReturn == 2) {
						String badTimeoutLink = singleLink.toString();
						timeOutLinks.add(badTimeoutLink);
					/* Else, if the returned value from isURLbroken is 404 (indicating that the link has a HTTP 
					 * response code of 404), store it in badResponseLinks.*/
					} else if (linkReturn == 404) {
						String badResponseLink = singleLink.toString();
						badResponseLinks.add(badResponseLink);
					}
				}
			}
			
			/* Output the resulting ArrayLists of broken links to a web page by passing them to the internal
			 * printBrokenLinks method. Also, pass the URL that was taken as a command-line argument so it can
			 * referenced in the printBrokenLinks method.*/
			printBrokenLinks(badResponseLinks, timeOutLinks, verifiedURL);	
		} 
		catch (Exception e) {
			System.out.println("Exception was thrown:");
			System.out.println("Exception message: " + e.getMessage());
		}
	}
	
	
	/**
	 * The following method has partly been taken (and adapted) from the source below: 
	 * http://computing.dcu.ie/~humphrys/Notes/Networks/java.html
	 * 
	 * This is an auxiliary method called by the processLinks method. It ascertains whether a link is 
	 * broken, based on it having a connection timeout of over 17 seconds or a HTTP response code of 404.
	 *
	 * @param  singleLink  the URL that is to be assessed
	 * @return  		   returns the response code resulting from having connected to the URL, or 1 or 2 
	 * 					   if the link times out. Returns 0 if another Exception was thrown
	 */
	private static int isURLbroken(String singleLink) throws Exception {
		HttpURLConnection c = null;
		try {
			// Convert singleLink to URL.
			URL url = new URL(singleLink);
			
			// Open the connection to url.
			c = (HttpURLConnection) url.openConnection();
			
			// Set the connection timeout: if unable to connect within 17 seconds, connection will time out.
			c.setConnectTimeout(17000);
			
			// Return the response code resulting from having connected to the URL.
			return c.getResponseCode();
		
		} catch (ConnectException e) {
			// Return 1 or 2 if the connection times out.
			return 1;
		} catch (SocketTimeoutException e) {
			return 2;
		} catch (Exception e) {
			// Return 0 if another Exception was thrown.
			return 0;
		}
		finally {
			// If the connection is open, disconnect.
			if (c != null) {
			c.disconnect();
			}
		}
	}
	
	/**
	 * This method outputs the resulting lists of broken links to a web page that can be browsed (offline)
	 * by clicking on the links.
	 *
	 * @param  brokenLinks1  an ArrayList of Strings containing the links with a HTTP response code of 404
	 * @param  brokenLinks2  an ArrayList of Strings containing the links that time out after 17 seconds
	 * @param  verifiedURL 	 the URL that was taken as a command-line argument and verified as a good URL
	 * @throws Exception
	 */
	public static void printBrokenLinks (ArrayList<String> brokenLinks1, ArrayList<String> brokenLinks2, String verifiedURL) throws Exception  {
		
		try {
			/* Create the file path that the downloaded page will be saved as (saving to the current
			 * working directory).*/
			String outputFilePath = System.getProperty("user.dir") + File.separator + "linkCheckerOutput.html";
			
			// Create the PrintWriter in order to print to a new file, passing in the outputFilePath created above.
			PrintWriter output = new PrintWriter(outputFilePath);
			
			// Format the HTML output displaying the project details for identification purposes.
			output.println("<p><h1><b>LinkChecker Programme<br></h1></b>");
			output.println("Course: CA651 - Introduction to Networks and Operating Systems <br>");
			output.println("Lecturer: Mark Humphrys <br>");
			output.println("Student name: Janice Brogan <br>");
			output.println("Student number: 14211493 <br></p>");
			output.println("<p>&nbsp;</p>");
			
			// Format the HTML output displaying the details of the URL that was tested.
			output.println("<p>");
			output.println("<b>Below is a demonstration of the LinkChecker programme working on the following URL: </b>");
			output.println("<a href=\"" + verifiedURL + "\">" + verifiedURL + "</a></p>");
			output.println("<p>&nbsp;</p>");
			output.println("<hr>");
		
			/* Format the HTML output displaying the links that had a HTTP response code of 404. Display a 'count' number
			 * beside each link printed.*/
			int count1 = 1;
			if (!(brokenLinks1.isEmpty())) {
				output.println("<p><h2>The following links were identified as having a HTTP response code of 404:</h2>");
				for(int i = 0; i < brokenLinks1.size(); i++){
					String outputURL1 = brokenLinks1.get(i);
					String outputHyperlink = "<a href=\"" + outputURL1 + "\">" + outputURL1 + "</a>";
					output.println("<h3>" + count1++ + ". <u>404 link identified:</u></h3>");
					output.println(outputHyperlink);
					output.println("<br>");
				}
				output.println("</p>");
			} else {
				// If no 404 links were found, print the following:
				output.println("<h2>No links with a HTTP response code of 404 were identified.</h2>");
				output.println("<br>");
			}
			
			output.println("<hr>");
			
			/* Format the HTML output displaying the links that had a connection time-out of over 17 seconds. Display a 'count' number
			 * beside each link printed.*/
			int count2 = 1;
			if (!(brokenLinks2.isEmpty())) {
				output.println("<p><h2>The following links were identified as having a connection time-out of over 17 seconds:</h2>");
				for(int i = 0; i < brokenLinks2.size(); i++){
					String outputURL2 = brokenLinks2.get(i);
					String outputHyperlink = "<a href=\"" + outputURL2 + "\">" + outputURL2 + "</a>";
					output.println("<h3>" + count2++ + ". <u>Timeout link identified:</u></h3>");
					output.println(outputHyperlink);
					output.println("<br>");
				}
				output.println("</p>");
			} else {
				// If no timeout links were found, print the following:
				output.println("<h2>No links with a connection timeout of over 17 seconds were identified.</h2>");
				output.println("<br>");
			}
			
			// Close the PrintWriter.
			output.close();
		} 
		catch (Exception e) {
			System.out.println("Exception was thrown:");
			System.out.println("Exception message: " + e.getMessage());
		}
	}
}
