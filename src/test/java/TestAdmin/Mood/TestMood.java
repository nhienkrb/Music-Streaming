package TestAdmin.Mood;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhymthwave.API_Admin.API_Mood;
import com.rhymthwave.MusicStreamingApplication;
import com.rhymthwave.ServiceAdmin.IMoodServiceAdmin;
import com.rhymthwave.entity.Mood;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(classes = MusicStreamingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestMood {

	private static final String ENDPOINT_URL = "/api/v1/admin/category/mood";

	@InjectMocks
	private API_Mood apiMood;

	@Mock
	private IMoodServiceAdmin iMoodService;

	@Autowired
	private MockMvc mockMvc;


	@Autowired
	public ObjectMapper objectMapper;


	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.apiMood).build();
	}


	@Test
	public void testMoodCreate_success() throws Exception {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		Mood mood = new Mood();
		mood.setMoodid(1);
		mood.setMoodname("CHILLove");
		mood.setCreateBy(String.valueOf(new Date()));
		mood.setModifiDate(new Date());
		mood.setCreateDate(new Date());
		mood.setModifiedBy("nhien");
		when(iMoodService.create(mood, request)).thenReturn(mood);

		String dto = objectMapper.writeValueAsString(mood);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders
						.post(ENDPOINT_URL)
						.content(dto).characterEncoding("utf-8")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	public void testMoodUpdate() throws Exception {
		// Mock HttpServletRequest
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		// Create a Mood object
		Mood mood = new Mood();
		mood.setMoodid(1);
		mood.setMoodname("CHILLove");
		mood.setCreateBy(String.valueOf(new Date()));
		mood.setModifiDate(new Date());
		mood.setCreateDate(new Date());
		mood.setModifiedBy("nhien");

		when(iMoodService.update(mood, request)).thenReturn(mood);

		String dto = objectMapper.writeValueAsString(mood);

		mockMvc.perform(MockMvcRequestBuilders
						.put(ENDPOINT_URL + "/" + mood.getMoodid())
						.content(dto).characterEncoding("utf-8")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk()); // Adjust the expected status code if needed
	}

	@Test
	public void testCreateMood() throws Exception {
		// Mock data
		Mood moodRequest = new Mood();
		moodRequest.setMoodname("CHILLove");
		// Add other properties as needed

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		// Mock the create method in your service
		when(iMoodService.create(moodRequest, request)).thenReturn(moodRequest);

		// Convert Mood object to JSON string
		String requestJson = objectMapper.writeValueAsString(moodRequest);

		// Perform the POST request and validate the response
		mockMvc.perform(MockMvcRequestBuilders
						.post(ENDPOINT_URL)  // Replace with your actual endpoint URL
						.content(requestJson).characterEncoding("utf-8")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Successfully"))
				.andExpect(jsonPath("$.data").exists());  // You may customize this based on your response structure
	}

	@Test
	public void testFindOneMood() throws Exception {
		// Mock data
		int moodId = 1;
		Mood mockMood = new Mood();
		mockMood.setMoodid(moodId);
		mockMood.setMoodname("CHILLove");
		// Add other properties as needed

		// Mock the findById method in your service
		when(iMoodService.findById(moodId)).thenReturn(mockMood);

		// Perform the GET request and validate the response
		mockMvc.perform(MockMvcRequestBuilders
						.get(ENDPOINT_URL + moodId)  // Replace with your actual endpoint URL
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data").exists()) ; // You may customize this based on your response structure
	}

}
