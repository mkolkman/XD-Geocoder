package io.corpus.gtt;

import java.text.ParseException;

import core.language.document.tweet.Tweet;
import core.gazetteer.Coordinate;
import io.corpus.csv.CsvTweetParser;

/**
 * Parses String input to Tweet for the GTT corpus 
 * 
 * @author s0201154
 */
public class GTTTweetParser implements CsvTweetParser {

	private static final int TABS_PER_LINE = 5;

	@Override
	public Tweet parse(String input) throws ParseException {
		if(input.equals("")){
			throw new ParseException("Empty line", 0);
		}		
		String[] parts = input.split("\t");
		int tabCount = parts.length - 1;
		if(tabCount < TABS_PER_LINE){
			throw new ParseException("Tabs per line. Expected: " + TABS_PER_LINE + ". Actual: " + tabCount, 0);
		}
		try{
			Tweet tweet = new Tweet();
			tweet.setUser(parts[0]);
			tweet.setDate(parts[1]);
			tweet.setGeotag(new Coordinate(parts[3], parts[4]));
			tweet.setText(parts[5]);
			
			return tweet;
		}catch(NumberFormatException e){
			throw new ParseException(e.getMessage(), 0);
		}
	}

}
