#pip install selenium beautifulsoup4 lxml requests
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import time
import re
import json
import os

def scroll_to_bottom(driver):
    """Scroll to the bottom of the page until no new content loads"""
    last_height = driver.execute_script("return document.body.scrollHeight")
    
    while True:
        # Scroll to bottom
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        
        # Wait for new content to load
        time.sleep(2)
        
        # Calculate new scroll height
        new_height = driver.execute_script("return document.body.scrollHeight")
        
        # Break if no new content loaded
        if new_height == last_height:
            break
            
        last_height = new_height

def extract_product_info(product):
    """Extract information from a single product element"""
    try:
        name = product.get('data-product-name', '')
        selling_method = product.get('data-selling-method', '')
        
        # getting the price
        flex_line = product.find('div', class_='flex-line')
        if flex_line:
            price_text = flex_line.find('div', class_='smallText')
            
            if price_text:
                # Get raw text for parsing
                raw_text = price_text.text.strip()
                
                # Extract price and unit using regex
                price_match = re.search(r'(\d+\.?\d*)\s*ש"ח\s*ל-\s*(\d+)\s*(ק"ג|גרם|יחידה)', raw_text)
                if price_match:
                    price = float(price_match.group(1))
                    quantity = int(price_match.group(2))
                    unit_raw = price_match.group(3)
                    unit = '' 
                    if unit_raw == 'ק"ג':
                        unit = 'kg'
                    elif unit_raw == 'גרם':
                        unit = 'gr'
                    elif unit_raw == 'יחידה':
                        # Skip products sold by unit with no weight
                        return None
                    else:
                        unit = unit_raw
                    
                    return {
                        'name': name,
                        'selling_method': selling_method,
                        'price': price,
                        'unit': unit,
                        'quantity': quantity
                    }
                else: 
                    print(f"No match for price text: '{raw_text}'")
            else:
                print(f"No price text found for product: {name}")
        else:
            print(f"No flex-line found for product: {name}")
            
    except Exception as e:
        print(f"Error processing product {name}: {e}")
    return None

def main():
    try:
        # Initialize Firefox driver
        driver = webdriver.Firefox()
        URL = "https://www.shufersal.co.il/online/he/קטגוריות/סופרמרקט/פירות-וירקות/c/A04?q=:relevance:categories-3:A0410"
        driver.get(URL)

        # Wait for initial products to load
        WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CLASS_NAME, "miglog-prod"))
        )

        # Scroll to load all products
        scroll_to_bottom(driver)

        # Get the updated page source after scrolling
        html_content = driver.page_source
        soup = BeautifulSoup(html_content, 'html.parser')
        products = soup.find_all('li', class_='miglog-prod')

        # Process all products
        product_list = []
        for product in products:
            product_info = extract_product_info(product)
            if product_info:
                product_list.append(product_info)

        # Print results
        print(f"Found {len(product_list)} products:")
        for product in product_list:
            print(f"Name: {product['name']}")
            print(f"Selling Method: {product['selling_method']}")
            print(f"Price: {product['price']} NIS per {product['quantity']} {product['unit']}")
            print("-" * 50)
        
        # Save results to JSON file
        current_path = os.getcwd() 
        output_path = os.path.join(current_path, 'backend\\mealbudget\\src\\main\\resources\\shufersal_scrapping\\shufersal_products.json')
        print(output_path)
        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(product_list, f, ensure_ascii=False, indent=4)
        
        print(f"Saved {len(product_list)} products to shufersal_products.json")

    except Exception as e:
        print(f"An error occurred in main: {e}")
    
    finally:
        driver.quit()

if __name__ == "__main__":
    main()