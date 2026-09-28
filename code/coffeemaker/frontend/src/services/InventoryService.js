import axios from "axios"
import { API_BASE_URL } from "./ApiConfig"

/** Base URL for the Inventory API - Correspond to methods in Backend's InventoryController. */
const REST_API_BASE_URL = API_BASE_URL + "/api/inventory"

/** GET Inventory - returns all inventory */
export const getInventory = () => axios.get(REST_API_BASE_URL)

/** PUT Inventory - updates the inventory */
export const updateInventory = (inventory) => axios.put(REST_API_BASE_URL, inventory)