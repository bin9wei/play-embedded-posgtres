Vagrant.configure("2") do |config|
  # https://app.vagrantup.com/centos/boxes/7
  config.vm.box = "centos/7"
  config.vm.network "forwarded_port", guest: 5432, host: 15432
  config.vm.provision 'shell', path: 'provision.sh'
end
