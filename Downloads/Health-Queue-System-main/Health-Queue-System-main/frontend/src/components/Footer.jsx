import React from 'react';

const Footer = () => {
  return (
    <footer className="bg-primary text-white py-6">
      <div className="container mx-auto px-4">
        <div className="flex flex-col md:flex-row justify-between items-center">
          <div className="mb-4 md:mb-0">
            <h3 className="text-xl font-bold">Health Queue System</h3>
            <p className="text-sm mt-1">Streamlining healthcare appointments</p>
          </div>
          <div className="text-sm">
            &copy; {new Date().getFullYear()} Health Queue System. All rights reserved.
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;