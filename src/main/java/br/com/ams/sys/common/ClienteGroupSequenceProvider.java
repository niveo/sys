package br.com.ams.sys.common;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import br.com.ams.sys.entity.AbstractClienteEmpresa;

//https://medium.com/blog-gilson-silva-ti/validando-cpf-cnpj-na-mesma-vari%C3%A1vel-com-bean-validation-4429a49e9bb5
public class ClienteGroupSequenceProvider implements DefaultGroupSequenceProvider<AbstractClienteEmpresa> {

	@Override
	public List<Class<?>> getValidationGroups(AbstractClienteEmpresa object) {
		List<Class<?>> groups = new ArrayList<Class<?>>();
		groups.add(AbstractClienteEmpresa.class);
		if (isPessoaSlecionada(object)) {
			groups.add(object.getTipoPessoa().getGroup());
		}
		return groups;
	}

	protected boolean isPessoaSlecionada(AbstractClienteEmpresa object) {
		return object != null && object.getTipoPessoa() != null;
	}

}
