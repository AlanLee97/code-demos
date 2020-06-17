import axios from 'axios';

let ip = "localhost";
let port = "8080";
let prefix = "http://" + ip + ":" + port + "/";

export function request(config) {
    const instance = axios.create({
            baseURL:prefix,
            timeout:100000
    });

    return instance(config);

}
