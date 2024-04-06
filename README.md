# Web Novels Grabber
This is a Java program that let the user download, from a certain popular web novel site, various information such as tags, novels and details about the novels.
This is a program created to get the info needed from my other program: [WebNovelTagsSearch](https://github.com/DeeJack/WebNovelTagsSearch), which is a website used to filter the novels, to find new, good novels.

<!-- GETTING STARTED -->
## Getting Started

To run this program just follow these steps.

### Prerequisites

The only program you need is Java 15.

### Installation

Download the latest jar file from the [releases](https://github.com/DeeJack/Web_Novel_Grabber/releases).

To run the program use:

```
java -jar web_novels_grabber.jar
```

<!-- USAGE EXAMPLES -->
## Usage
![Usage example.](/readme/usage1.png "Usage.")

After choosing the task you want to execute, you need to insert the URL of the site, the only supported site for now is a certain famous site, which I don't want to mention for precaution (more like to cover my ass).
After the URL, you also need to insert the timeout. This is needed to not overwork (and not aourse suspicions) the local network and the site. The timeout is in milliseconds (1000 ms -> 1 second).
During the execution, the program will create the output files in the directory you started the program from.

## Result example
![Result example.](/readme/result.png "Result.")
