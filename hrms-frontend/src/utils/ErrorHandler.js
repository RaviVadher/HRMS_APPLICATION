class ErrorHandler {

    static getMessage(error) {

        if (error.response) {

            const data = error.response.data;
            console.log(error);
            if (data && data.message) {
                return data.message;
            }

            return "Server Error occured";
        }

        if (error.request) {
            return "server not responding. Try again"
        }

        return "Unexpected err occurred"
    }

}

export default ErrorHandler;