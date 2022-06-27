package hu.progmatic.spotilive;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class MockMvcTestHelper {
  public static RequestBuilder testRequest(MockMvc mockMvc) {
    return new RequestBuilder(mockMvc);
  }

  public static abstract class AbstractRequestBuilder {
    protected final MockMvc mockMvc;
    protected final String resource;

    public AbstractRequestBuilder(MockMvc mockMvc, String resource) {
      this.mockMvc = mockMvc;
      this.resource = resource;
    }

    public abstract MockMvcTestHelper.AssertationBuilder buildRequest() throws Exception;
  }

  public static class PostRequestBuilder extends AbstractRequestBuilder {
    private final List<FormParam> params = new ArrayList<>();

    public PostRequestBuilder(MockMvc mockMvc, String resource) {
      super(mockMvc, resource);
    }

    @Override
    public AssertationBuilder buildRequest() throws Exception {
      return new AssertationBuilder(
          mockMvc.perform(
              MockMvcRequestBuilders.post(resource)
                  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                  .content(
                      params.stream().map(FormParam::toUrlEncoded).collect(Collectors.joining("&"))
                  )
          )
      );
    }

    public PostRequestBuilder addFormParameter(String key, String value) {
      params.add(new FormParam(key, value));
      return this;
    }

    private record FormParam(String key, String value) {
      String toUrlEncoded() {
        try {
          return URLEncoder.encode(key, StandardCharsets.UTF_8.name()) +
              "="
              + URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  public static class GetRequestBuilder extends AbstractRequestBuilder {
    public GetRequestBuilder(MockMvc mockMvc, String resource) {
      super(mockMvc, resource);
    }

    @Override
    public AssertationBuilder buildRequest() throws Exception {
      return new AssertationBuilder(
          mockMvc.perform(
              MockMvcRequestBuilders.get(resource)
          )
      );
    }
  }

  public static class RequestBuilder {
    private final MockMvc mockMvc;

    public RequestBuilder(MockMvc mockMvc) {
      this.mockMvc = mockMvc;
    }

    public PostRequestBuilder postRequestBuilder(String resource) {
      return new PostRequestBuilder(mockMvc, resource);
    }

    public AssertationBuilder postRequest(String resource) throws Exception {
      return new PostRequestBuilder(mockMvc, resource).buildRequest();
    }

    public AssertationBuilder getRequest(String resource) throws Exception {
      return new GetRequestBuilder(mockMvc, resource).buildRequest();
    }
  }

  @SuppressWarnings("UnusedReturnValue")
  public static class AssertationBuilder {
    private ResultActions resultActions;

    public AssertationBuilder(ResultActions resultActions) {
      this.resultActions = resultActions;
    }

    public AssertationBuilder expectStatusIsOk() throws Exception {
      resultActions = resultActions.andExpect(
          MockMvcResultMatchers.status().isOk()
      );
      return this;
    }

    public AssertationBuilder expectForwardedToUrl(String url) throws Exception {
      resultActions = resultActions.andExpect(
          MockMvcResultMatchers.forwardedUrl(url)
      );
      return this;
    }

    public AssertationBuilder expectContentContainsString(String text) throws Exception {
      resultActions = resultActions.andExpect(
          MockMvcResultMatchers.content().string(containsString(text))
      );
      return this;
    }
    public AssertationBuilder expectContentNotContainsString(String text) throws Exception {
      resultActions = resultActions.andExpect(
          MockMvcResultMatchers.content().string(not(containsString(text)))
      );
      return this;
    }

    public AssertationBuilder expectRedirectedToUrlPattern(String urlPattern) throws Exception {
      resultActions = resultActions
          .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
          .andExpect(MockMvcResultMatchers.redirectedUrlPattern(urlPattern));
      return this;
    }
    public AssertationBuilder printRequest() throws Exception {
      resultActions = resultActions.andDo(print());
      return this;
    }
  }
}
