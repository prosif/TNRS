package org.iplantc.tnrs.transform;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.transformer.AbstractTransformer;

public class TaxamatchOuputTransformer extends AbstractMessageAwareTransformer{


	

	public Object transform(MuleMessage message, String outputEncoding)
	throws TransformerException {
		try {
		   
			String result_json = iTransform(message.getPayloadAsString());
			
			message.setPayload(result_json);
			message.setProperty("Content-Length", new Integer(result_json.length()));
			
			return message;

		}catch(Exception ex) {
			ex.printStackTrace();
			throw new TransformerException(this, ex);
		}
	}
	
	public String iTransform(String json) {
		JSONObject tnrsOutput = (JSONObject)JSONSerializer.toJSON(json);

		JSONObject output = new JSONObject();

		JSONArray data = tnrsOutput.getJSONArray("data");

		JSONArray outputArray = new JSONArray();

		for(int i=0; i < data.size(); i++) {

			JSONArray cur_array = data.getJSONArray(i);

			for(int k=0; k < cur_array.size();k++) {
				JSONObject cur_result = cur_array.getJSONObject(k);
				JSONObject item = new JSONObject();

				item.put("group", Integer.toString(i));
				item.put("acceptedName","");
				item.put("acceptedAuthor",cur_result.optString("Accepted_author","").toString().replace("null", ""));
				item.put("nameSubmitted", cur_result.optString("ScientificName_submitted","").toString().replace("null", ""));
				item.put("url", cur_result.optString("NameSourceUrl","").replace("\\", "").toString().replace("null", ""));
				item.put("nameScientific", cur_result.optString("Lowest_scientificName_matched","").toString().replace("null", ""));
				item.put("scientificScore",cur_result.optString("Lowest_sciName_matched_score", "").toString().replace("null", ""));
				item.put("matchedFamily", cur_result.optString("Family_matched", "").toString().replace("null", ""));
				item.put("matchedFamilyScore", cur_result.optString("Family_matched_score", "").toString().replace("null", ""));
				item.put("authorAttributed", cur_result.optString("Canonical_author","").toString().replace("null", ""));
				item.put("family", cur_result.optString("Accepted_family","").toString().replace("null", ""));
				item.put("genus",cur_result.optString("Genus_matched","").toString().replace("null", ""));
				item.put("genusScore",cur_result.optString("Genus_matched_score", "").toString().replace("null", ""));
				item.put("speciesMatched", cur_result.optString("SpecificEpithet_matched", "").toString().replace("null", ""));
				item.put("speciesMatchedScore", cur_result.optString("SpecificEpithet_matched_score", "").toString().replace("null", ""));
				item.put("infraspecific1Rank", cur_result.optString("infraspecific1Rank", "").toString().replace("null", ""));
				item.put("infraspecific1Epithet",  cur_result.optString("infraspecific1Epithet", "").toString().replace("null", ""));
				item.put("infraspecific1EpithetScore",  cur_result.optString("infraspecific1Score", "").toString().replace("null", ""));
				item.put("infraspecific2Rank", cur_result.optString("infraspecific2Rank", "").toString().replace("null", ""));
				item.put("infraspecific2Epithet",  cur_result.optString("infraspecific2Epithet", "").toString().replace("null", ""));
				item.put("infraspecific2EpithetScore",  cur_result.optString("infraspecific2EpithetScore", "").toString().replace("null", ""));
				item.put("epithet", cur_result.optString("SpecificEpithet_matched","").toString().replace("null", ""));
				item.put("epithetScore", cur_result.optString("SpecificEpithet_matched_score","").toString().replace("null", ""));
				item.put("author",cur_result.optString("Author_matched","").toString().replace("null", "") );
				item.put("authorScore",cur_result.optString("Author_matched_score", "").toString().replace("null", ""));
				item.put("annotation", cur_result.optString("Status", "").toString().replace("null", ""));
				item.put("unmatched", cur_result.optString("Unmatched", "").toString().replace("null", ""));
				item.put("overall", cur_result.optString("Overall_match_score", "").toString().replace("null", ""));
				item.put("acceptedName",cur_result.optString("Accepted_name", "").replace("null", ""));
				
				if(cur_result.optString("Acceptance", "").replace("null", "").trim().equalsIgnoreCase("A")) {
					item.put("acceptance","Accepted" );
				}else if(cur_result.optString("Acceptance", "").replace("null", "").trim().equalsIgnoreCase("S")) {
					item.put("acceptance","Synonym" );
				}else {
					item.put("acceptance","No opinion" );
				}
				item.put("familySubmitted", cur_result.optString("family", "").replace("null", "") );
				
				if(k==0) {
					item.put("selected", new Boolean(true));
				}else {
					item.put("selected", new Boolean(false));
				}
				item.put("acceptedNameUrl",  cur_result.optString("Accepted_name_SourceUrl", "").toString().replace("null", ""));

				outputArray.add(item);
			}



		}

		output.put("items", outputArray);
		return output.toString();
		
		
	}




}
