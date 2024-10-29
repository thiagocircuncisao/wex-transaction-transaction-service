package thiagocircuncisao.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

   @Override
   public void addViewControllers(ViewControllerRegistry registry) {
       registry.addViewController("/").setViewName("redirect:/swagger-ui/index.html");
   }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
